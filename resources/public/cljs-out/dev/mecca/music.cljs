(ns ^:figwheel-hooks mecca.music
  (:require
   [cljs.core.async :refer [<! timeout chan put! close!]]
   [reagent.core :as r]
   [re-frame.core :as rf :refer [subscribe dispatch]])
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]]))

(defn ^:export audio-context []
  (if js/window.AudioContext.
    (js/window.AudioContext.)
    (js/window.webkitAudioContext.)))

(defonce audiocontext (r/atom (audio-context)))

(defn ^:export current-time [context]
  (.-currentTime context))

(def lookahead 25.0)

(def scheduleAheadTime 0.1)

(defn scheduler []
  (let [next-note-time (subscribe [:next-note-time])
        current-note (subscribe [:current-note])]
    (if (< @next-note-time
         (+ scheduleAheadTime
            (current-time @audiocontext)))
      (dispatch [:schedule-note @current-note @next-note-time])
      (dispatch [:next-note]))))


(defn mmbass-minor-triad [time root]
  [{:time time :instrument 15, :pitch root}
   {:time (+ time 1.5) :instrument 15, :pitch root}
   {:time (+ time 2) :instrument 15, :pitch (+ root 3)}
   {:time (+ time 3) :instrument 15, :pitch (+ root 7)}
   {:time (+ time 4.5) :instrument 15, :pitch root}
   {:time (+ time 5) :instrument 15, :pitch root}
   {:time (+ time 5.5) :instrument 15, :pitch root}
   {:time (+ time 6) :instrument 15, :pitch (+ root 3)}
   {:time (+ time 7) :instrument 15, :pitch (+ root 7)}])

(defn mmbass-major-triad [time root]
  [{:time time :instrument 15, :pitch root}
   {:time (+ time 1.5) :instrument 15, :pitch root}
   {:time (+ time 2) :instrument 15, :pitch (+ root 4)}
   {:time (+ time 3) :instrument 15, :pitch (+ root 7)}
   {:time (+ time 4.5) :instrument 15, :pitch root}
   {:time (+ time 5) :instrument 15, :pitch root}
   {:time (+ time 5.5) :instrument 15, :pitch root}
   {:time (+ time 6) :instrument 15, :pitch (+ root 4)}
   {:time (+ time 7) :instrument 15, :pitch (+ root 7)}])

(defn mm8 [time pitch]
  (apply concat
   (for [beat (range 0 8 2)]
     (mmbass-minor-triad (+ beat time) pitch))))

(defn mmbass []
  (concat
   (mmbass-minor-triad 0 64)
   (mmbass-major-triad 8 60)
   (mmbass-major-triad 16 62)
   (mmbass-minor-triad 24 64)))

(defn mario-jump? []
  (let [beat (subscribe [:current-position])
        notes (subscribe [:notes])
        jump (subscribe [:mario-jump])]
    (when (and @(subscribe [:playing?])
           (zero? @jump))
      (if (< 0 (count (filter #(= (:time %) (inc @beat))
                              @notes)))
      (dispatch [:jump!])))))

(defn song-done? []
  (let [notes (subscribe [:notes])
        playing? @(subscribe [:playing?])
        now (.-currentTime @audiocontext)
        length (apply max (map #(:time %) @notes))
        started (subscribe [:play-start])
        elapsed (- (current-time @audiocontext) @started)
        beat-length (/ 60 @(subscribe [:tempo]))
        current-beat (/ elapsed beat-length)
        last-drawn-pos (subscribe [:current-position])]
    (when playing?
      (if (< length current-beat)
        (dispatch [:play-off])
        (if (< @last-drawn-pos current-beat)
          (do (dispatch [:move-mario])
            (dispatch [:advance-position])))))
    (mario-jump?)))

(defn dispatch-timer-event []
  (dispatch [:tick!])
      (song-done?)
  (scheduler))

(defonce do-timer
  (js/setInterval dispatch-timer-event 30))

(defn load-sound [named-url]
  (let [out (chan)
        req (js/XMLHttpRequest.)]
    (set! (.-responseType req) "arraybuffer")
    (set! (.-onload req) (fn [e]
                           (if (= (.-status req) 200)
                             (do (put! out (assoc named-url :buffer (.-response req)))
                                 (close! out))
                             (close! out))))
    (.open req "GET" (:url named-url) true)
    (.send req)
    out))

(defn decode [named-url]
  (let [out (chan)]
    (if (:buffer named-url)
      (do
        (.decodeAudioData
         @audiocontext (:buffer named-url)
         (fn [decoded-buffer]
           (put! out (assoc named-url :decoded-buffer decoded-buffer))
           (close! out))
         (fn []
           (.error js/console "Error loading file " (prn named-url))
           (close! out))))
      (close! out))
    out))

(defn buffer-source [buffer]
  (let [source (.createBufferSource @audiocontext)]
    (set! (.-buffer source) buffer)
    source))

(defn get-and-decode [named-url]
  (go
    (when-let [s (<! (load-sound named-url))]
      (<! (decode s)))))

(defn load-samples []
  (go-loop [result {}
            sounds (range 1 27)]
    (if-not (nil? (first sounds))
      (let [sound (first sounds)                   ; for Github Pages - remove the '/mecca/resources/public' to run locally
            decoded-buffer (<! (get-and-decode {:url (str "/audio/" sound ".mp3")
                                                :sound sound}))]
        (prn sound)
        (prn decoded-buffer)
        (recur (assoc result sound decoded-buffer)
               (rest sounds)))
      result)))

(defonce loading-samples
  (go
    (def samples (<! (load-samples)))
    (prn "Samples loaded")))

(defn pitch->rate [midi-num]
  (case midi-num
    55 0.5
    56 0.5297315471796479
    57 0.5612310241546867
    58 0.5946035575013607
    59 0.6299605249474368
    60 0.6674199270850174
    61 0.7071067811865477
    62 0.7491535384383409
    63 0.7937005259840998
    64 0.8408964152537146
    65 0.8908987181403394
    66 0.9438743126816935
    67 1
    68 1.0594630943592953
    69 1.122462048309373
    70 1.1892071150027212
    71 1.2599210498948734
    72 1.3348398541700346
    73 1.4142135623730954
    74 1.498307076876682
    75 1.5874010519682
    76 1.6817928305074297
    77 1.7817974362806792
    78 1.8877486253633877
    79 2
    80 2.1189261887185906
    81 2.244924096618746
    82 2.3784142300054425
    83 2.519842099789747))

(defn play-sample [instrument pitch]
  (let [context audiocontext
        audio-buffer (:decoded-buffer (get samples instrument))
        sample-source (.createBufferSource @context)
        compressor (.createDynamicsCompressor @context)
        analyser (.createAnalyser @context)]
    (set! (.-buffer sample-source) audio-buffer)
    (.setValueAtTime
     (.-playbackRate sample-source)
     (pitch->rate pitch)
     (.-currentTime @context))
    (.connect sample-source analyser)
    (.connect sample-source (.-destination @context))
    (.start sample-source)
    sample-source))

(defn play-at [instrument pitch time]
  (let [context audiocontext
        audio-buffer (:decoded-buffer (get samples instrument))
        sample-source (.createBufferSource @context)]
    (set! (.-buffer sample-source) audio-buffer)
    (.setValueAtTime
     (.-playbackRate sample-source)
     (pitch->rate (if (< 83 pitch)
                    (- pitch 24)
                    pitch))
     time)
    (.connect sample-source (.-destination @context))
    (.start sample-source time)
    sample-source))

(defn play-song! []
  (let [notes (subscribe [:notes])
        now (.-currentTime @audiocontext)
        tempo (subscribe [:tempo])]
    (dispatch [:reset-position])
    (doall (for [{:keys [time instrument pitch]} @notes]
             (play-at instrument pitch (+ now (* (/ 60 @tempo) time)))))))

(defn get-bytes! [analyser freq-data]
  (.getByteFrequencyData analyser freq-data)
  freq-data)


(def city [{:time 67.5, :instrument 1, :pitch 74}{:time 67, :instrument 1, :pitch 71}{:time 66, :instrument 1, :pitch 74}{:time 65.5, :instrument 1, :pitch 76}{:time 64.5, :instrument 1, :pitch 78}{:time 60.5, :instrument 1, :pitch 73}{:time 59.5, :instrument 1, :pitch 73}{:time 59, :instrument 1, :pitch 72}{:time 58, :instrument 1, :pitch 72}{:time 57, :instrument 1, :pitch 72}{:time 56.5, :instrument 1, :pitch 72}{:time 55.5, :instrument 1, :pitch 72}{:time 50.5, :instrument 1, :pitch 66}{:time 49.5, :instrument 1, :pitch 67}{:time 49, :instrument 1, :pitch 66}{:time 48.5, :instrument 1, :pitch 64}{:time 47.5, :instrument 1, :pitch 62}{:time 46.5, :instrument 1, :pitch 69}{:time 43, :instrument 1, :pitch 66}{:time 42, :instrument 1, :pitch 69}{:time 41.5, :instrument 1, :pitch 66}{:time 41, :instrument 1, :pitch 69}{:time 40.5, :instrument 1, :pitch 69}{:time 39.5, :instrument 1, :pitch 69}{:time 35.5, :instrument 1, :pitch 62}{:time 35, :instrument 1, :pitch 62}{:time 34, :instrument 1, :pitch 62}{:time 31.5, :instrument 1, :pitch 74}{:time 31, :instrument 1, :pitch 73}{:time 27.5, :instrument 1, :pitch 73}{:time 27, :instrument 1, :pitch 72}{:time 26, :instrument 1, :pitch 72}{:time 25, :instrument 1, :pitch 72}{:time 24.5, :instrument 1, :pitch 72}{:time 23.5, :instrument 1, :pitch 72}{:time 18.5, :instrument 1, :pitch 66}{:time 17.5, :instrument 1, :pitch 67}{:time 17, :instrument 1, :pitch 66}{:time 16.5, :instrument 1, :pitch 64}{:time 15.5, :instrument 1, :pitch 62}{:time 15, :instrument 1, :pitch 69}{:time 11, :instrument 1, :pitch 66}{:time 10, :instrument 1, :pitch 69}{:time 9.5, :instrument 1, :pitch 66}{:time 9, :instrument 1, :pitch 69}{:time 8.5, :instrument 1, :pitch 69}{:time 7.5, :instrument 1, :pitch 69}{:time 67.5, :instrument 5, :pitch 62}{:time 67, :instrument 5, :pitch 64}{:time 66.5, :instrument 5, :pitch 66}{:time 66, :instrument 5, :pitch 67}{:time 63.5, :instrument 5, :pitch 66}{:time 63, :instrument 5, :pitch 64}{:time 59.5, :instrument 5, :pitch 64}{:time 59, :instrument 5, :pitch 66}{:time 58, :instrument 5, :pitch 67}{:time 57, :instrument 5, :pitch 67}{:time 56.5, :instrument 5, :pitch 67}{:time 55.5, :instrument 5, :pitch 67}{:time 50.5, :instrument 5, :pitch 62}{:time 49.5, :instrument 5, :pitch 64}{:time 49, :instrument 5, :pitch 62}{:time 48.5, :instrument 5, :pitch 59}{:time 47.5, :instrument 5, :pitch 57}{:time 47, :instrument 5, :pitch 66}{:time 43, :instrument 5, :pitch 62}{:time 42, :instrument 5, :pitch 66}{:time 41.5, :instrument 5, :pitch 62}{:time 41, :instrument 5, :pitch 66}{:time 40.5, :instrument 5, :pitch 66}{:time 39.5, :instrument 5, :pitch 66}{:time 105, :instrument 13, :pitch 71}{:time 104.5, :instrument 13, :pitch 69}{:time 104, :instrument 13, :pitch 71}{:time 103.5, :instrument 13, :pitch 72}{:time 103, :instrument 13, :pitch 74}{:time 102.5, :instrument 13, :pitch 76}{:time 102, :instrument 13, :pitch 77}{:time 101.5, :instrument 13, :pitch 72}{:time 101, :instrument 13, :pitch 74}{:time 100.5, :instrument 13, :pitch 76}{:time 100, :instrument 13, :pitch 77}{:time 35.5, :instrument 5, :pitch 62}{:time 35, :instrument 5, :pitch 64}{:time 34.5, :instrument 5, :pitch 66}{:time 34, :instrument 5, :pitch 67}{:time 31.5, :instrument 5, :pitch 66}{:time 31, :instrument 5, :pitch 64}{:time 27.5, :instrument 5, :pitch 64}{:time 27, :instrument 5, :pitch 66}{:time 26, :instrument 5, :pitch 67}{:time 25, :instrument 5, :pitch 67}{:time 24.5, :instrument 5, :pitch 67}{:time 23.5, :instrument 5, :pitch 67}{:time 18.5, :instrument 5, :pitch 62}{:time 17.5, :instrument 5, :pitch 64}{:time 17, :instrument 5, :pitch 62}{:time 16.5, :instrument 5, :pitch 59}{:time 15.5, :instrument 5, :pitch 57}{:time 15, :instrument 5, :pitch 66}{:time 11, :instrument 5, :pitch 62}{:time 10, :instrument 5, :pitch 66}{:time 9.5, :instrument 5, :pitch 62}{:time 9, :instrument 5, :pitch 66}{:time 8.5, :instrument 5, :pitch 66}{:time 7.5, :instrument 5, :pitch 66}{:time 129.5, :instrument 8, :pitch 64}{:time 131, :instrument 8, :pitch 62}{:time 128, :instrument 8, :pitch 65}{:time 147, :instrument 8, :pitch 59}{:time 145.5, :instrument 8, :pitch 62}{:time 144, :instrument 8, :pitch 60}{:time 147.5, :instrument 2, :pitch 55}{:time 147, :instrument 2, :pitch 59}{:time 147.5, :instrument 13, :pitch 74}{:time 147.5, :instrument 15, :pitch 55}{:time 123.5, :instrument 15, :pitch 55}{:time 122.5, :instrument 15, :pitch 55}{:time 122, :instrument 15, :pitch 55}{:time 121.5, :instrument 15, :pitch 55}{:time 120.5, :instrument 15, :pitch 55}{:time 120, :instrument 15, :pitch 55}{:time 119.5, :instrument 15, :pitch 55}{:time 118.5, :instrument 15, :pitch 55}{:time 118, :instrument 15, :pitch 55}{:time 117.5, :instrument 15, :pitch 55}{:time 116.5, :instrument 15, :pitch 55}{:time 116, :instrument 15, :pitch 55}{:time 116, :instrument 2, :pitch 67}{:time 115.5, :instrument 2, :pitch 69}{:time 115, :instrument 2, :pitch 69}{:time 114.5, :instrument 2, :pitch 71}{:time 114, :instrument 2, :pitch 72}{:time 115.5, :instrument 13, :pitch 77}{:time 115, :instrument 13, :pitch 77}{:time 114.5, :instrument 13, :pitch 77}{:time 114, :instrument 13, :pitch 77}{:time 113.5, :instrument 13, :pitch 77}{:time 110.5, :instrument 2, :pitch 60}{:time 110, :instrument 2, :pitch 69}{:time 109.5, :instrument 2, :pitch 55}{:time 109, :instrument 2, :pitch 65}{:time 108.5, :instrument 2, :pitch 71}{:time 108, :instrument 2, :pitch 57}{:time 120, :instrument 13, :pitch 77}{:time 119.5, :instrument 13, :pitch 77}{:time 119, :instrument 13, :pitch 77}{:time 118.5, :instrument 13, :pitch 77}{:time 118, :instrument 13, :pitch 77}{:time 117.5, :instrument 13, :pitch 77}{:time 117, :instrument 13, :pitch 77}{:time 116.5, :instrument 13, :pitch 77}{:time 116, :instrument 13, :pitch 77}{:time 115.5, :instrument 13, :pitch 76}{:time 111, :instrument 7, :pitch 59}{:time 109.5, :instrument 7, :pitch 61}{:time 108, :instrument 7, :pitch 62}{:time 111, :instrument 8, :pitch 59}{:time 109.5, :instrument 8, :pitch 61}{:time 108, :instrument 8, :pitch 62}{:time 111, :instrument 14, :pitch 59}{:time 109.5, :instrument 14, :pitch 61}{:time 108, :instrument 14, :pitch 62}{:time 111, :instrument 15, :pitch 59}{:time 109.5, :instrument 15, :pitch 61}{:time 108, :instrument 15, :pitch 62}{:time 107.5, :instrument 13, :pitch 74}{:time 107, :instrument 13, :pitch 74}{:time 106.5, :instrument 13, :pitch 74}{:time 106, :instrument 13, :pitch 74}{:time 105.5, :instrument 13, :pitch 74}{:time 107.5, :instrument 15, :pitch 55}{:time 107, :instrument 15, :pitch 55}{:time 106.5, :instrument 15, :pitch 55}{:time 106, :instrument 15, :pitch 55}{:time 105.5, :instrument 15, :pitch 55}{:time 105, :instrument 15, :pitch 55}{:time 104.5, :instrument 15, :pitch 55}{:time 104, :instrument 15, :pitch 55}{:time 103.5, :instrument 15, :pitch 55}{:time 103, :instrument 15, :pitch 55}{:time 102.5, :instrument 15, :pitch 55}{:time 102, :instrument 15, :pitch 55}{:time 101.5, :instrument 15, :pitch 55}{:time 101, :instrument 15, :pitch 55}{:time 100.5, :instrument 15, :pitch 55}{:time 100, :instrument 15, :pitch 55}{:time 99.5, :instrument 2, :pitch 59}{:time 99, :instrument 2, :pitch 72}{:time 98.5, :instrument 2, :pitch 59}{:time 98, :instrument 2, :pitch 59}{:time 97.5, :instrument 2, :pitch 59}{:time 97, :instrument 2, :pitch 71}{:time 96.5, :instrument 2, :pitch 59}{:time 96, :instrument 2, :pitch 57}{:time 95.5, :instrument 2, :pitch 59}{:time 95, :instrument 2, :pitch 69}{:time 94.5, :instrument 2, :pitch 59}{:time 94, :instrument 2, :pitch 59}{:time 93.5, :instrument 2, :pitch 59}{:time 93, :instrument 2, :pitch 69}{:time 92, :instrument 2, :pitch 59}{:time 91.5, :instrument 2, :pitch 60}{:time 91, :instrument 2, :pitch 69}{:time 90.5, :instrument 2, :pitch 59}{:time 99.5, :instrument 13, :pitch 72}{:time 99, :instrument 13, :pitch 74}{:time 98.5, :instrument 13, :pitch 76}{:time 98, :instrument 13, :pitch 77}{:time 97.5, :instrument 13, :pitch 72}{:time 97, :instrument 13, :pitch 74}{:time 96.5, :instrument 13, :pitch 76}{:time 96, :instrument 13, :pitch 77}{:time 95.5, :instrument 13, :pitch 77}{:time 95, :instrument 13, :pitch 77}{:time 94.5, :instrument 13, :pitch 77}{:time 94, :instrument 13, :pitch 77}{:time 93.5, :instrument 13, :pitch 77}{:time 93, :instrument 13, :pitch 77}{:time 92.5, :instrument 13, :pitch 77}{:time 92, :instrument 13, :pitch 77}{:time 91.5, :instrument 13, :pitch 77}{:time 91, :instrument 13, :pitch 77}{:time 90.5, :instrument 13, :pitch 77}{:time 90, :instrument 13, :pitch 77}{:time 89.5, :instrument 13, :pitch 77}{:time 89, :instrument 13, :pitch 77}{:time 89.5, :instrument 2, :pitch 59}{:time 89, :instrument 2, :pitch 69}{:time 88, :instrument 2, :pitch 57}{:time 87.5, :instrument 2, :pitch 59}{:time 87, :instrument 2, :pitch 67}{:time 86.5, :instrument 2, :pitch 59}{:time 86, :instrument 2, :pitch 59}{:time 85.5, :instrument 2, :pitch 59}{:time 85, :instrument 2, :pitch 69}{:time 84, :instrument 2, :pitch 59}{:time 83.5, :instrument 2, :pitch 59}{:time 83, :instrument 2, :pitch 65}{:time 82.5, :instrument 2, :pitch 59}{:time 82, :instrument 2, :pitch 59}{:time 81.5, :instrument 2, :pitch 59}{:time 81, :instrument 2, :pitch 69}{:time 80, :instrument 2, :pitch 57}{:time 79.5, :instrument 2, :pitch 62}{:time 79, :instrument 2, :pitch 69}{:time 78.5, :instrument 2, :pitch 60}{:time 78, :instrument 2, :pitch 60}{:time 77.5, :instrument 2, :pitch 60}{:time 77, :instrument 2, :pitch 71}{:time 76, :instrument 2, :pitch 60}{:time 75.5, :instrument 2, :pitch 64}{:time 75, :instrument 2, :pitch 72}{:time 74.5, :instrument 2, :pitch 64}{:time 73.5, :instrument 2, :pitch 64}{:time 73, :instrument 2, :pitch 71}{:time 88.5, :instrument 13, :pitch 77}{:time 88, :instrument 13, :pitch 78}{:time 87, :instrument 13, :pitch 79}{:time 86, :instrument 13, :pitch 79}{:time 85, :instrument 13, :pitch 81}{:time 84, :instrument 13, :pitch 79}{:time 83, :instrument 13, :pitch 77}{:time 82, :instrument 13, :pitch 76}{:time 81, :instrument 13, :pitch 77}{:time 80, :instrument 13, :pitch 79}{:time 79, :instrument 13, :pitch 77}{:time 78, :instrument 13, :pitch 77}{:time 77, :instrument 13, :pitch 77}{:time 76, :instrument 13, :pitch 79}{:time 75, :instrument 13, :pitch 76}{:time 74, :instrument 13, :pitch 76}{:time 73, :instrument 13, :pitch 76}{:time 72, :instrument 13, :pitch 81}{:time 43.5, :instrument 2, :pitch 62}{:time 43, :instrument 2, :pitch 71}{:time 42.5, :instrument 2, :pitch 60}{:time 41.5, :instrument 2, :pitch 60}{:time 41, :instrument 2, :pitch 71}{:time 99.5, :instrument 15, :pitch 55}{:time 99, :instrument 15, :pitch 55}{:time 98.5, :instrument 15, :pitch 55}{:time 98, :instrument 15, :pitch 55}{:time 97.5, :instrument 15, :pitch 55}{:time 97, :instrument 15, :pitch 55}{:time 96.5, :instrument 15, :pitch 55}{:time 96, :instrument 15, :pitch 55}{:time 95.5, :instrument 15, :pitch 55}{:time 95, :instrument 15, :pitch 55}{:time 94.5, :instrument 15, :pitch 55}{:time 94, :instrument 15, :pitch 55}{:time 93.5, :instrument 15, :pitch 55}{:time 93, :instrument 15, :pitch 55}{:time 92.5, :instrument 15, :pitch 55}{:time 92, :instrument 15, :pitch 55}{:time 91.5, :instrument 15, :pitch 55}{:time 91, :instrument 15, :pitch 55}{:time 90.5, :instrument 15, :pitch 55}{:time 90, :instrument 15, :pitch 55}{:time 89.5, :instrument 15, :pitch 55}{:time 72, :instrument 2, :pitch 62}{:time 71.5, :instrument 2, :pitch 62}{:time 71, :instrument 2, :pitch 62}{:time 70.5, :instrument 2, :pitch 62}{:time 70, :instrument 2, :pitch 62}{:time 69.5, :instrument 2, :pitch 62}{:time 69, :instrument 2, :pitch 71}{:time 68, :instrument 2, :pitch 62}{:time 67.5, :instrument 2, :pitch 65}{:time 67, :instrument 2, :pitch 72}{:time 66.5, :instrument 2, :pitch 64}{:time 66, :instrument 2, :pitch 62}{:time 65.5, :instrument 2, :pitch 62}{:time 65, :instrument 2, :pitch 71}{:time 64, :instrument 2, :pitch 62}{:time 63.5, :instrument 2, :pitch 62}{:time 63, :instrument 2, :pitch 71}{:time 62.5, :instrument 2, :pitch 62}{:time 62, :instrument 2, :pitch 60}{:time 61.5, :instrument 2, :pitch 65}{:time 61, :instrument 2, :pitch 71}{:time 60, :instrument 2, :pitch 64}{:time 59.5, :instrument 2, :pitch 65}{:time 59, :instrument 2, :pitch 72}{:time 58.5, :instrument 2, :pitch 64}{:time 58, :instrument 2, :pitch 64}{:time 57.5, :instrument 2, :pitch 64}{:time 57, :instrument 2, :pitch 76}{:time 56, :instrument 2, :pitch 62}{:time 55.5, :instrument 2, :pitch 62}{:time 55, :instrument 2, :pitch 74}{:time 54.5, :instrument 2, :pitch 62}{:time 54, :instrument 2, :pitch 62}{:time 53.5, :instrument 2, :pitch 62}{:time 53, :instrument 2, :pitch 74}{:time 52, :instrument 2, :pitch 64}{:time 51.5, :instrument 2, :pitch 64}{:time 51, :instrument 2, :pitch 74}{:time 50.5, :instrument 2, :pitch 64}{:time 50, :instrument 2, :pitch 64}{:time 49.5, :instrument 2, :pitch 62}{:time 49, :instrument 2, :pitch 72}{:time 48, :instrument 2, :pitch 62}{:time 47.5, :instrument 2, :pitch 60}{:time 47, :instrument 2, :pitch 71}{:time 46.5, :instrument 2, :pitch 62}{:time 45.5, :instrument 2, :pitch 60}{:time 45, :instrument 2, :pitch 71}{:time 89, :instrument 15, :pitch 55}{:time 88.5, :instrument 15, :pitch 55}{:time 88, :instrument 15, :pitch 67}{:time 87.5, :instrument 15, :pitch 57}{:time 87, :instrument 15, :pitch 69}{:time 86.5, :instrument 15, :pitch 57}{:time 85.5, :instrument 15, :pitch 57}{:time 85, :instrument 15, :pitch 69}{:time 84, :instrument 15, :pitch 57}{:time 83.5, :instrument 15, :pitch 69}{:time 83, :instrument 15, :pitch 71}{:time 82.5, :instrument 15, :pitch 73}{:time 82, :instrument 15, :pitch 74}{:time 81.5, :instrument 15, :pitch 62}{:time 80.5, :instrument 15, :pitch 62}{:time 81, :instrument 15, :pitch 69}{:time 80, :instrument 15, :pitch 74}{:time 79.5, :instrument 15, :pitch 67}{:time 79, :instrument 15, :pitch 73}{:time 78.5, :instrument 15, :pitch 67}{:time 78, :instrument 15, :pitch 71}{:time 77.5, :instrument 15, :pitch 67}{:time 76, :instrument 15, :pitch 67}{:time 75.5, :instrument 15, :pitch 69}{:time 75, :instrument 15, :pitch 71}{:time 74, :instrument 15, :pitch 66}{:time 73.5, :instrument 15, :pitch 71}{:time 72, :instrument 15, :pitch 71}{:time 71.5, :instrument 15, :pitch 73}{:time 71, :instrument 15, :pitch 74}{:time 70.5, :instrument 15, :pitch 76}{:time 70, :instrument 15, :pitch 78}{:time 69.5, :instrument 15, :pitch 62}{:time 69, :instrument 15, :pitch 69}{:time 68.5, :instrument 15, :pitch 62}{:time 68, :instrument 15, :pitch 78}{:time 67.5, :instrument 15, :pitch 79}{:time 67, :instrument 15, :pitch 78}{:time 66.5, :instrument 15, :pitch 76}{:time 66, :instrument 15, :pitch 74}{:time 65.5, :instrument 15, :pitch 62}{:time 65, :instrument 15, :pitch 74}{:time 64, :instrument 15, :pitch 62}{:time 63.5, :instrument 15, :pitch 62}{:time 62.5, :instrument 15, :pitch 62}{:time 61.5, :instrument 15, :pitch 62}{:time 63, :instrument 15, :pitch 73}{:time 61, :instrument 15, :pitch 73}{:time 60, :instrument 15, :pitch 62}{:time 59.5, :instrument 15, :pitch 62}{:time 59, :instrument 15, :pitch 72}{:time 58.5, :instrument 15, :pitch 62}{:time 57.5, :instrument 15, :pitch 62}{:time 57, :instrument 15, :pitch 72}{:time 56, :instrument 15, :pitch 62}{:time 55.5, :instrument 15, :pitch 69}{:time 55, :instrument 15, :pitch 67}{:time 54.5, :instrument 15, :pitch 69}{:time 54, :instrument 15, :pitch 67}{:time 53.5, :instrument 15, :pitch 62}{:time 53, :instrument 15, :pitch 69}{:time 52, :instrument 15, :pitch 62}{:time 51.5, :instrument 15, :pitch 62}{:time 51, :instrument 15, :pitch 69}{:time 50.5, :instrument 15, :pitch 62}{:time 49.5, :instrument 15, :pitch 62}{:time 49, :instrument 15, :pitch 69}{:time 48, :instrument 15, :pitch 62}{:time 47.5, :instrument 15, :pitch 69}{:time 47, :instrument 15, :pitch 67}{:time 46.5, :instrument 15, :pitch 69}{:time 46, :instrument 15, :pitch 67}{:time 45.5, :instrument 15, :pitch 62}{:time 45, :instrument 15, :pitch 69}{:time 44, :instrument 15, :pitch 62}{:time 43.5, :instrument 15, :pitch 74}{:time 43, :instrument 15, :pitch 72}{:time 42.5, :instrument 15, :pitch 62}{:time 41.5, :instrument 15, :pitch 62}{:time 41, :instrument 15, :pitch 66}{:time 40, :instrument 15, :pitch 62}{:time 39.5, :instrument 15, :pitch 62}{:time 39, :instrument 15, :pitch 69}{:time 38.5, :instrument 15, :pitch 62}{:time 37.5, :instrument 15, :pitch 62}{:time 37, :instrument 15, :pitch 66}{:time 36, :instrument 15, :pitch 62}{:time 35.5, :instrument 15, :pitch 69}{:time 35, :instrument 15, :pitch 67}{:time 34.5, :instrument 15, :pitch 62}{:time 33, :instrument 15, :pitch 74}{:time 31, :instrument 15, :pitch 73}{:time 29, :instrument 15, :pitch 73}{:time 27, :instrument 15, :pitch 72}{:time 25, :instrument 15, :pitch 72}{:time 0, :instrument 15, :pitch 62}{:time 0, :instrument 2, :pitch 60}{:time 0.5, :instrument 2, :pitch 60}{:time 1, :instrument 2, :pitch 71}{:time 1.5, :instrument 2, :pitch 60}{:time 2, :instrument 2, :pitch 62}{:time 2.5, :instrument 2, :pitch 60}{:time 3, :instrument 2, :pitch 69}{:time 3.5, :instrument 2, :pitch 62}{:time 1, :instrument 15, :pitch 69}{:time 1.5, :instrument 15, :pitch 62}{:time 2.5, :instrument 15, :pitch 62}{:time 3, :instrument 15, :pitch 69}{:time 3.5, :instrument 15, :pitch 62}{:time 4, :instrument 2, :pitch 60}{:time 4.5, :instrument 2, :pitch 60}{:time 5, :instrument 2, :pitch 69}{:time 5.5, :instrument 2, :pitch 60}{:time 6, :instrument 2, :pitch 60}{:time 6.5, :instrument 2, :pitch 60}{:time 7, :instrument 2, :pitch 72}{:time 7.5, :instrument 2, :pitch 60}{:time 4.5, :instrument 15, :pitch 62}{:time 5, :instrument 15, :pitch 69}{:time 5.5, :instrument 15, :pitch 62}{:time 6.5, :instrument 15, :pitch 62}{:time 7, :instrument 15, :pitch 69}{:time 7.5, :instrument 15, :pitch 62}{:time 8, :instrument 15, :pitch 62}{:time 8, :instrument 2, :pitch 60}{:time 8.5, :instrument 2, :pitch 60}{:time 9, :instrument 2, :pitch 74}{:time 9.5, :instrument 2, :pitch 60}{:time 10, :instrument 2, :pitch 60}{:time 10.5, :instrument 2, :pitch 60}{:time 11, :instrument 2, :pitch 74}{:time 11.5, :instrument 2, :pitch 60}{:time 12, :instrument 2, :pitch 60}{:time 12.5, :instrument 2, :pitch 60}{:time 13, :instrument 2, :pitch 76}{:time 13.5, :instrument 2, :pitch 60}{:time 14, :instrument 2, :pitch 60}{:time 14.5, :instrument 2, :pitch 60}{:time 15, :instrument 2, :pitch 72}{:time 15.5, :instrument 2, :pitch 60}{:time 18, :instrument 2, :pitch 60}{:time 16, :instrument 2, :pitch 60}{:time 17, :instrument 2, :pitch 74}{:time 17.5, :instrument 2, :pitch 60}{:time 18.5, :instrument 2, :pitch 60}{:time 19, :instrument 2, :pitch 72}{:time 19.5, :instrument 2, :pitch 60}{:time 20.5, :instrument 2, :pitch 60}{:time 21, :instrument 2, :pitch 71}{:time 21.5, :instrument 2, :pitch 60}{:time 22.5, :instrument 2, :pitch 60}{:time 23, :instrument 2, :pitch 71}{:time 23.5, :instrument 2, :pitch 60}{:time 24, :instrument 2, :pitch 60}{:time 25, :instrument 2, :pitch 74}{:time 25.5, :instrument 2, :pitch 62}{:time 26, :instrument 2, :pitch 62}{:time 26.5, :instrument 2, :pitch 60}{:time 27, :instrument 2, :pitch 74}{:time 27.5, :instrument 2, :pitch 60}{:time 28, :instrument 2, :pitch 60}{:time 28.5, :instrument 2, :pitch 60}{:time 29, :instrument 2, :pitch 72}{:time 29.5, :instrument 2, :pitch 62}{:time 30, :instrument 2, :pitch 60}{:time 30.5, :instrument 2, :pitch 60}{:time 31, :instrument 2, :pitch 71}{:time 31.5, :instrument 2, :pitch 62}{:time 32, :instrument 2, :pitch 60}{:time 33, :instrument 2, :pitch 71}{:time 33.5, :instrument 2, :pitch 60}{:time 34, :instrument 2, :pitch 60}{:time 34.5, :instrument 2, :pitch 60}{:time 35, :instrument 2, :pitch 72}{:time 35.5, :instrument 2, :pitch 60}{:time 36, :instrument 2, :pitch 60}{:time 37, :instrument 2, :pitch 72}{:time 37.5, :instrument 2, :pitch 60}{:time 38, :instrument 2, :pitch 62}{:time 38.5, :instrument 2, :pitch 60}{:time 39, :instrument 2, :pitch 72}{:time 39.5, :instrument 2, :pitch 60}{:time 39.5, :instrument 2, :pitch 60}{:time 40, :instrument 2, :pitch 60}{:time 44, :instrument 2, :pitch 60}{:time 44.5, :instrument 2, :pitch 60}{:time 44.5, :instrument 2, :pitch 60}{:time 8.5, :instrument 15, :pitch 62}{:time 9, :instrument 15, :pitch 69}{:time 9.5, :instrument 15, :pitch 62}{:time 10.5, :instrument 15, :pitch 62}{:time 11, :instrument 15, :pitch 69}{:time 11.5, :instrument 15, :pitch 62}{:time 12.5, :instrument 15, :pitch 62}{:time 13, :instrument 15, :pitch 69}{:time 13.5, :instrument 15, :pitch 62}{:time 14, :instrument 15, :pitch 62}{:time 15, :instrument 15, :pitch 69}{:time 15.5, :instrument 15, :pitch 62}{:time 16.5, :instrument 15, :pitch 62}{:time 17, :instrument 15, :pitch 69}{:time 17.5, :instrument 15, :pitch 62}{:time 18, :instrument 15, :pitch 62}{:time 19, :instrument 15, :pitch 69}{:time 19.5, :instrument 15, :pitch 62}{:time 20, :instrument 15, :pitch 62}{:time 21, :instrument 15, :pitch 69}{:time 21.5, :instrument 15, :pitch 62}{:time 22.5, :instrument 15, :pitch 62}{:time 23, :instrument 15, :pitch 69}{:time 23.5, :instrument 15, :pitch 62}{:time 24, :instrument 15, :pitch 62}{:time 25.5, :instrument 15, :pitch 62}{:time 26.5, :instrument 15, :pitch 62}{:time 27.5, :instrument 15, :pitch 62}{:time 28, :instrument 15, :pitch 62}{:time 29.5, :instrument 15, :pitch 62}{:time 30, :instrument 15, :pitch 62}{:time 30.5, :instrument 15, :pitch 62}{:time 31.5, :instrument 15, :pitch 62}{:time 32, :instrument 15, :pitch 62}{:time 33.5, :instrument 15, :pitch 62}{:time 116, :instrument 2, :pitch 59}{:time 117, :instrument 2, :pitch 67}{:time 117.5, :instrument 2, :pitch 59}{:time 118, :instrument 2, :pitch 59}{:time 118.5, :instrument 2, :pitch 59}{:time 119, :instrument 2, :pitch 69}{:time 119.5, :instrument 2, :pitch 59}{:time 120, :instrument 2, :pitch 59}{:time 120.5, :instrument 2, :pitch 59}{:time 121, :instrument 2, :pitch 69}{:time 121.5, :instrument 2, :pitch 57}{:time 122, :instrument 2, :pitch 59}{:time 122.5, :instrument 2, :pitch 59}{:time 123, :instrument 2, :pitch 71}{:time 123.5, :instrument 2, :pitch 59}{:time 124, :instrument 2, :pitch 59}{:time 125, :instrument 2, :pitch 72}{:time 125.5, :instrument 2, :pitch 59}{:time 126, :instrument 2, :pitch 59}{:time 126.5, :instrument 2, :pitch 59}{:time 127, :instrument 2, :pitch 71}{:time 127.5, :instrument 2, :pitch 59}{:time 128, :instrument 2, :pitch 59}{:time 128.5, :instrument 2, :pitch 59}{:time 129, :instrument 2, :pitch 71}{:time 129.5, :instrument 2, :pitch 59}{:time 120, :instrument 13, :pitch 77}{:time 120.5, :instrument 13, :pitch 77}{:time 121, :instrument 13, :pitch 77}{:time 121.5, :instrument 13, :pitch 77}{:time 122, :instrument 13, :pitch 77}{:time 122.5, :instrument 13, :pitch 77}{:time 123, :instrument 13, :pitch 77}{:time 123.5, :instrument 13, :pitch 77}{:time 124, :instrument 15, :pitch 62}{:time 124.5, :instrument 15, :pitch 62}{:time 125.5, :instrument 15, :pitch 62}{:time 126, :instrument 15, :pitch 62}{:time 126.5, :instrument 15, :pitch 62}{:time 127.5, :instrument 15, :pitch 62}{:time 128, :instrument 15, :pitch 57}{:time 128.5, :instrument 15, :pitch 57}{:time 129.5, :instrument 15, :pitch 57}{:time 130, :instrument 15, :pitch 57}{:time 130.5, :instrument 15, :pitch 57}{:time 131.5, :instrument 15, :pitch 57}{:time 132, :instrument 15, :pitch 55}{:time 132.5, :instrument 15, :pitch 55}{:time 133.5, :instrument 15, :pitch 55}{:time 134, :instrument 15, :pitch 55}{:time 134.5, :instrument 15, :pitch 55}{:time 130, :instrument 2, :pitch 59}{:time 130.5, :instrument 2, :pitch 59}{:time 131, :instrument 2, :pitch 69}{:time 131.5, :instrument 2, :pitch 59}{:time 132, :instrument 2, :pitch 59}{:time 132.5, :instrument 2, :pitch 59}{:time 133.5, :instrument 2, :pitch 59}{:time 134, :instrument 2, :pitch 59}{:time 134.5, :instrument 2, :pitch 59}{:time 124, :instrument 13, :pitch 77}{:time 124.5, :instrument 13, :pitch 77}{:time 125.5, :instrument 13, :pitch 77}{:time 126, :instrument 13, :pitch 77}{:time 126.5, :instrument 13, :pitch 76}{:time 127.5, :instrument 13, :pitch 77}{:time 125, :instrument 13, :pitch 77}{:time 127, :instrument 13, :pitch 77}{:time 128, :instrument 13, :pitch 77}{:time 128.5, :instrument 13, :pitch 77}{:time 129, :instrument 13, :pitch 77}{:time 129.5, :instrument 13, :pitch 77}{:time 130, :instrument 13, :pitch 77}{:time 130.5, :instrument 13, :pitch 77}{:time 131, :instrument 13, :pitch 77}{:time 131.5, :instrument 13, :pitch 77}{:time 132, :instrument 13, :pitch 77}{:time 132.5, :instrument 13, :pitch 77}{:time 133, :instrument 13, :pitch 77}{:time 133.5, :instrument 13, :pitch 77}{:time 134, :instrument 13, :pitch 77}{:time 134.5, :instrument 13, :pitch 77}{:time 135.5, :instrument 15, :pitch 55}{:time 136, :instrument 15, :pitch 55}{:time 136.5, :instrument 15, :pitch 55}{:time 137.5, :instrument 15, :pitch 55}{:time 138, :instrument 15, :pitch 55}{:time 138.5, :instrument 15, :pitch 55}{:time 139.5, :instrument 15, :pitch 55}{:time 140, :instrument 15, :pitch 62}{:time 140.5, :instrument 15, :pitch 62}{:time 141.5, :instrument 15, :pitch 62}{:time 142, :instrument 15, :pitch 62}{:time 142.5, :instrument 15, :pitch 62}{:time 143.5, :instrument 15, :pitch 62}{:time 144, :instrument 15, :pitch 57}{:time 144.5, :instrument 15, :pitch 57}{:time 145.5, :instrument 15, :pitch 57}{:time 146, :instrument 15, :pitch 57}{:time 144, :instrument 2, :pitch 65}{:time 144.5, :instrument 2, :pitch 64}{:time 145, :instrument 2, :pitch 60}{:time 145.5, :instrument 2, :pitch 67}{:time 146, :instrument 2, :pitch 64}{:time 146.5, :instrument 2, :pitch 60}{:time 135, :instrument 13, :pitch 77}{:time 135.5, :instrument 13, :pitch 77}{:time 136, :instrument 13, :pitch 77}{:time 136.5, :instrument 13, :pitch 77}{:time 137, :instrument 13, :pitch 77}{:time 137.5, :instrument 13, :pitch 77}{:time 138, :instrument 13, :pitch 76}{:time 138.5, :instrument 13, :pitch 76}{:time 139, :instrument 13, :pitch 77}{:time 139.5, :instrument 13, :pitch 77}{:time 140, :instrument 13, :pitch 77}{:time 140.5, :instrument 13, :pitch 77}{:time 133, :instrument 2, :pitch 69}{:time 135, :instrument 2, :pitch 71}{:time 137, :instrument 2, :pitch 69}{:time 135.5, :instrument 2, :pitch 57}{:time 136, :instrument 2, :pitch 59}{:time 136.5, :instrument 2, :pitch 59}{:time 137.5, :instrument 2, :pitch 59}{:time 138, :instrument 2, :pitch 59}{:time 138.5, :instrument 2, :pitch 60}{:time 139.5, :instrument 2, :pitch 69}{:time 139, :instrument 2, :pitch 67}{:time 139.5, :instrument 2, :pitch 59}{:time 140, :instrument 2, :pitch 55}{:time 140.5, :instrument 2, :pitch 55}{:time 141, :instrument 2, :pitch 71}{:time 141.5, :instrument 2, :pitch 59}{:time 142, :instrument 2, :pitch 59}{:time 142.5, :instrument 2, :pitch 57}{:time 143, :instrument 2, :pitch 69}{:time 143, :instrument 2, :pitch 60}{:time 143.5, :instrument 2, :pitch 57}{:time 141, :instrument 13, :pitch 77}{:time 141.5, :instrument 13, :pitch 76}{:time 142, :instrument 13, :pitch 76}{:time 142.5, :instrument 13, :pitch 76}{:time 143, :instrument 13, :pitch 77}{:time 143.5, :instrument 13, :pitch 76}{:time 144, :instrument 13, :pitch 76}{:time 144.5, :instrument 13, :pitch 76}{:time 145, :instrument 13, :pitch 76}{:time 145.5, :instrument 13, :pitch 76}{:time 146, :instrument 13, :pitch 76}{:time 146.5, :instrument 13, :pitch 74}{:time 147, :instrument 13, :pitch 74}{:time 144, :instrument 9, :pitch 62}{:time 145.5, :instrument 9, :pitch 62}{:time 147, :instrument 9, :pitch 59}{:time 146.5, :instrument 15, :pitch 57}])