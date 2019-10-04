// Compiled by ClojureScript 1.10.339 {:static-fns true, :optimize-constants true}
goog.provide('mecca.music');
goog.require('cljs.core');
goog.require('cljs.core.constants');
goog.require('cljs.core.async');
goog.require('reagent.core');
goog.require('re_frame.core');
mecca.music.audio_context = (function mecca$music$audio_context(){
if(cljs.core.truth_(window.AudioContext)){
return (new window.AudioContext());
} else {
return (new window.webkitAudioContext());
}
});
goog.exportSymbol('mecca.music.audio_context', mecca.music.audio_context);
if((typeof mecca !== 'undefined') && (typeof mecca.music !== 'undefined') && (typeof mecca.music.audiocontext !== 'undefined')){
} else {
mecca.music.audiocontext = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(mecca.music.audio_context());
}
mecca.music.current_time = (function mecca$music$current_time(context){
return context.currentTime;
});
goog.exportSymbol('mecca.music.current_time', mecca.music.current_time);
mecca.music.mario_jump_QMARK_ = (function mecca$music$mario_jump_QMARK_(){
var beat = cljs.core.deref((function (){var G__19396 = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$current_DASH_position], null);
return (re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1 ? re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(G__19396) : re_frame.core.subscribe.call(null,G__19396));
})());
var notes = cljs.core.deref((function (){var G__19397 = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$notes], null);
return (re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1 ? re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(G__19397) : re_frame.core.subscribe.call(null,G__19397));
})());
if(cljs.core.truth_(cljs.core.deref((function (){var G__19398 = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$playing_QMARK_], null);
return (re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1 ? re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(G__19398) : re_frame.core.subscribe.call(null,G__19398));
})()))){
if(((0) < cljs.core.count(cljs.core.filter.cljs$core$IFn$_invoke$arity$2(((function (beat,notes){
return (function (p1__19395_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.cst$kw$time.cljs$core$IFn$_invoke$arity$1(p1__19395_SHARP_),beat);
});})(beat,notes))
,cljs.core.deref((function (){var G__19399 = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$notes], null);
return (re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1 ? re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(G__19399) : re_frame.core.subscribe.call(null,G__19399));
})()))))){
var G__19400 = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$jump_BANG_], null);
return (re_frame.core.dispatch.cljs$core$IFn$_invoke$arity$1 ? re_frame.core.dispatch.cljs$core$IFn$_invoke$arity$1(G__19400) : re_frame.core.dispatch.call(null,G__19400));
} else {
var G__19401 = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$down_BANG_], null);
return (re_frame.core.dispatch.cljs$core$IFn$_invoke$arity$1 ? re_frame.core.dispatch.cljs$core$IFn$_invoke$arity$1(G__19401) : re_frame.core.dispatch.call(null,G__19401));
}
} else {
return null;
}
});
mecca.music.mario_move = (function mecca$music$mario_move(){
var playing_QMARK_ = cljs.core.deref((function (){var G__19402 = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$playing_QMARK_], null);
return (re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1 ? re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(G__19402) : re_frame.core.subscribe.call(null,G__19402));
})());
var beat = cljs.core.deref((function (){var G__19403 = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$current_DASH_position], null);
return (re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1 ? re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(G__19403) : re_frame.core.subscribe.call(null,G__19403));
})());
if(cljs.core.truth_(playing_QMARK_)){
var G__19404 = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$move_DASH_mario,((40) * beat)], null);
return (re_frame.core.dispatch.cljs$core$IFn$_invoke$arity$1 ? re_frame.core.dispatch.cljs$core$IFn$_invoke$arity$1(G__19404) : re_frame.core.dispatch.call(null,G__19404));
} else {
return null;
}
});
mecca.music.song_done_QMARK_ = (function mecca$music$song_done_QMARK_(){
var notes = (function (){var G__19406 = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$notes], null);
return (re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1 ? re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(G__19406) : re_frame.core.subscribe.call(null,G__19406));
})();
var playing_QMARK_ = cljs.core.deref((function (){var G__19407 = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$playing_QMARK_], null);
return (re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1 ? re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(G__19407) : re_frame.core.subscribe.call(null,G__19407));
})());
var now = cljs.core.deref(mecca.music.audiocontext).currentTime;
var length = cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.max,cljs.core.map.cljs$core$IFn$_invoke$arity$2(((function (notes,playing_QMARK_,now){
return (function (p1__19405_SHARP_){
return cljs.core.cst$kw$time.cljs$core$IFn$_invoke$arity$1(p1__19405_SHARP_);
});})(notes,playing_QMARK_,now))
,cljs.core.deref(notes)));
var started = cljs.core.deref((function (){var G__19408 = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$play_DASH_start], null);
return (re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1 ? re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(G__19408) : re_frame.core.subscribe.call(null,G__19408));
})());
var elapsed = (mecca.music.current_time(cljs.core.deref(mecca.music.audiocontext)) - started);
var beat_length = ((60) / cljs.core.deref((function (){var G__19409 = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$tempo], null);
return (re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1 ? re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(G__19409) : re_frame.core.subscribe.call(null,G__19409));
})()));
var current_beat = (elapsed / beat_length);
if(cljs.core.truth_(playing_QMARK_)){
if((length < current_beat)){
var G__19410_19412 = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$play_DASH_off], null);
(re_frame.core.dispatch.cljs$core$IFn$_invoke$arity$1 ? re_frame.core.dispatch.cljs$core$IFn$_invoke$arity$1(G__19410_19412) : re_frame.core.dispatch.call(null,G__19410_19412));
} else {
if(((started + beat_length) < now)){
var G__19411_19413 = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$advance_DASH_position], null);
(re_frame.core.dispatch.cljs$core$IFn$_invoke$arity$1 ? re_frame.core.dispatch.cljs$core$IFn$_invoke$arity$1(G__19411_19413) : re_frame.core.dispatch.call(null,G__19411_19413));
} else {
}
}
} else {
}

mecca.music.mario_move();

return mecca.music.mario_jump_QMARK_();
});
mecca.music.dispatch_timer_event = (function mecca$music$dispatch_timer_event(){
var G__19414_19415 = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$tick_BANG_], null);
(re_frame.core.dispatch.cljs$core$IFn$_invoke$arity$1 ? re_frame.core.dispatch.cljs$core$IFn$_invoke$arity$1(G__19414_19415) : re_frame.core.dispatch.call(null,G__19414_19415));

return mecca.music.song_done_QMARK_();
});
if((typeof mecca !== 'undefined') && (typeof mecca.music !== 'undefined') && (typeof mecca.music.do_timer !== 'undefined')){
} else {
mecca.music.do_timer = setInterval(mecca.music.dispatch_timer_event,(150));
}
mecca.music.load_sound = (function mecca$music$load_sound(named_url){
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$0();
var req = (new XMLHttpRequest());
req.responseType = "arraybuffer";

req.onload = ((function (out,req){
return (function (e){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(req.status,(200))){
cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2(out,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(named_url,cljs.core.cst$kw$buffer,req.response));

return cljs.core.async.close_BANG_(out);
} else {
return cljs.core.async.close_BANG_(out);
}
});})(out,req))
;

req.open("GET",cljs.core.cst$kw$url.cljs$core$IFn$_invoke$arity$1(named_url),true);

req.send();

return out;
});
mecca.music.decode = (function mecca$music$decode(named_url){
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_(cljs.core.cst$kw$buffer.cljs$core$IFn$_invoke$arity$1(named_url))){
cljs.core.deref(mecca.music.audiocontext).decodeAudioData(cljs.core.cst$kw$buffer.cljs$core$IFn$_invoke$arity$1(named_url),((function (out){
return (function (decoded_buffer){
cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2(out,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(named_url,cljs.core.cst$kw$decoded_DASH_buffer,decoded_buffer));

return cljs.core.async.close_BANG_(out);
});})(out))
,((function (out){
return (function (){
console.error("Error loading file ",cljs.core.prn.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([named_url], 0)));

return cljs.core.async.close_BANG_(out);
});})(out))
);
} else {
cljs.core.async.close_BANG_(out);
}

return out;
});
mecca.music.buffer_source = (function mecca$music$buffer_source(buffer){
var source = cljs.core.deref(mecca.music.audiocontext).createBufferSource();
source.buffer = buffer;

return source;
});
mecca.music.get_and_decode = (function mecca$music$get_and_decode(named_url){
var c__15583__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run(((function (c__15583__auto__){
return (function (){
var f__15584__auto__ = (function (){var switch__15400__auto__ = ((function (c__15583__auto__){
return (function (state_19427){
var state_val_19428 = (state_19427[(1)]);
if((state_val_19428 === (1))){
var inst_19416 = mecca.music.load_sound(named_url);
var state_19427__$1 = state_19427;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_19427__$1,(2),inst_19416);
} else {
if((state_val_19428 === (2))){
var inst_19418 = (state_19427[(7)]);
var inst_19418__$1 = (state_19427[(2)]);
var state_19427__$1 = (function (){var statearr_19429 = state_19427;
(statearr_19429[(7)] = inst_19418__$1);

return statearr_19429;
})();
if(cljs.core.truth_(inst_19418__$1)){
var statearr_19430_19438 = state_19427__$1;
(statearr_19430_19438[(1)] = (3));

} else {
var statearr_19431_19439 = state_19427__$1;
(statearr_19431_19439[(1)] = (4));

}

return cljs.core.cst$kw$recur;
} else {
if((state_val_19428 === (3))){
var inst_19418 = (state_19427[(7)]);
var inst_19420 = mecca.music.decode(inst_19418);
var state_19427__$1 = state_19427;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_19427__$1,(6),inst_19420);
} else {
if((state_val_19428 === (4))){
var state_19427__$1 = state_19427;
var statearr_19432_19440 = state_19427__$1;
(statearr_19432_19440[(2)] = null);

(statearr_19432_19440[(1)] = (5));


return cljs.core.cst$kw$recur;
} else {
if((state_val_19428 === (5))){
var inst_19425 = (state_19427[(2)]);
var state_19427__$1 = state_19427;
return cljs.core.async.impl.ioc_helpers.return_chan(state_19427__$1,inst_19425);
} else {
if((state_val_19428 === (6))){
var inst_19422 = (state_19427[(2)]);
var state_19427__$1 = state_19427;
var statearr_19433_19441 = state_19427__$1;
(statearr_19433_19441[(2)] = inst_19422);

(statearr_19433_19441[(1)] = (5));


return cljs.core.cst$kw$recur;
} else {
return null;
}
}
}
}
}
}
});})(c__15583__auto__))
;
return ((function (switch__15400__auto__,c__15583__auto__){
return (function() {
var mecca$music$get_and_decode_$_state_machine__15401__auto__ = null;
var mecca$music$get_and_decode_$_state_machine__15401__auto____0 = (function (){
var statearr_19434 = [null,null,null,null,null,null,null,null];
(statearr_19434[(0)] = mecca$music$get_and_decode_$_state_machine__15401__auto__);

(statearr_19434[(1)] = (1));

return statearr_19434;
});
var mecca$music$get_and_decode_$_state_machine__15401__auto____1 = (function (state_19427){
while(true){
var ret_value__15402__auto__ = (function (){try{while(true){
var result__15403__auto__ = switch__15400__auto__(state_19427);
if(cljs.core.keyword_identical_QMARK_(result__15403__auto__,cljs.core.cst$kw$recur)){
continue;
} else {
return result__15403__auto__;
}
break;
}
}catch (e19435){if((e19435 instanceof Object)){
var ex__15404__auto__ = e19435;
var statearr_19436_19442 = state_19427;
(statearr_19436_19442[(5)] = ex__15404__auto__);


cljs.core.async.impl.ioc_helpers.process_exception(state_19427);

return cljs.core.cst$kw$recur;
} else {
throw e19435;

}
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__15402__auto__,cljs.core.cst$kw$recur)){
var G__19443 = state_19427;
state_19427 = G__19443;
continue;
} else {
return ret_value__15402__auto__;
}
break;
}
});
mecca$music$get_and_decode_$_state_machine__15401__auto__ = function(state_19427){
switch(arguments.length){
case 0:
return mecca$music$get_and_decode_$_state_machine__15401__auto____0.call(this);
case 1:
return mecca$music$get_and_decode_$_state_machine__15401__auto____1.call(this,state_19427);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
mecca$music$get_and_decode_$_state_machine__15401__auto__.cljs$core$IFn$_invoke$arity$0 = mecca$music$get_and_decode_$_state_machine__15401__auto____0;
mecca$music$get_and_decode_$_state_machine__15401__auto__.cljs$core$IFn$_invoke$arity$1 = mecca$music$get_and_decode_$_state_machine__15401__auto____1;
return mecca$music$get_and_decode_$_state_machine__15401__auto__;
})()
;})(switch__15400__auto__,c__15583__auto__))
})();
var state__15585__auto__ = (function (){var statearr_19437 = (f__15584__auto__.cljs$core$IFn$_invoke$arity$0 ? f__15584__auto__.cljs$core$IFn$_invoke$arity$0() : f__15584__auto__.call(null));
(statearr_19437[(6)] = c__15583__auto__);

return statearr_19437;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__15585__auto__);
});})(c__15583__auto__))
);

return c__15583__auto__;
});
mecca.music.load_samples = (function mecca$music$load_samples(){
var c__15583__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run(((function (c__15583__auto__){
return (function (){
var f__15584__auto__ = (function (){var switch__15400__auto__ = ((function (c__15583__auto__){
return (function (state_19472){
var state_val_19473 = (state_19472[(1)]);
if((state_val_19473 === (1))){
var inst_19444 = cljs.core.PersistentHashMap.EMPTY;
var inst_19445 = cljs.core.range.cljs$core$IFn$_invoke$arity$2((1),(27));
var inst_19446 = inst_19444;
var inst_19447 = inst_19445;
var state_19472__$1 = (function (){var statearr_19474 = state_19472;
(statearr_19474[(7)] = inst_19446);

(statearr_19474[(8)] = inst_19447);

return statearr_19474;
})();
var statearr_19475_19487 = state_19472__$1;
(statearr_19475_19487[(2)] = null);

(statearr_19475_19487[(1)] = (2));


return cljs.core.cst$kw$recur;
} else {
if((state_val_19473 === (2))){
var inst_19447 = (state_19472[(8)]);
var inst_19449 = cljs.core.first(inst_19447);
var inst_19450 = (inst_19449 == null);
var inst_19451 = cljs.core.not(inst_19450);
var state_19472__$1 = state_19472;
if(inst_19451){
var statearr_19476_19488 = state_19472__$1;
(statearr_19476_19488[(1)] = (4));

} else {
var statearr_19477_19489 = state_19472__$1;
(statearr_19477_19489[(1)] = (5));

}

return cljs.core.cst$kw$recur;
} else {
if((state_val_19473 === (3))){
var inst_19470 = (state_19472[(2)]);
var state_19472__$1 = state_19472;
return cljs.core.async.impl.ioc_helpers.return_chan(state_19472__$1,inst_19470);
} else {
if((state_val_19473 === (4))){
var inst_19453 = (state_19472[(9)]);
var inst_19447 = (state_19472[(8)]);
var inst_19453__$1 = cljs.core.first(inst_19447);
var inst_19454 = [cljs.core.cst$kw$url,cljs.core.cst$kw$sound];
var inst_19455 = ["/mecca/resources/public/audio/",cljs.core.str.cljs$core$IFn$_invoke$arity$1(inst_19453__$1),".mp3"].join('');
var inst_19456 = [inst_19455,inst_19453__$1];
var inst_19457 = cljs.core.PersistentHashMap.fromArrays(inst_19454,inst_19456);
var inst_19458 = mecca.music.get_and_decode(inst_19457);
var state_19472__$1 = (function (){var statearr_19478 = state_19472;
(statearr_19478[(9)] = inst_19453__$1);

return statearr_19478;
})();
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_19472__$1,(7),inst_19458);
} else {
if((state_val_19473 === (5))){
var inst_19446 = (state_19472[(7)]);
var state_19472__$1 = state_19472;
var statearr_19479_19490 = state_19472__$1;
(statearr_19479_19490[(2)] = inst_19446);

(statearr_19479_19490[(1)] = (6));


return cljs.core.cst$kw$recur;
} else {
if((state_val_19473 === (6))){
var inst_19468 = (state_19472[(2)]);
var state_19472__$1 = state_19472;
var statearr_19480_19491 = state_19472__$1;
(statearr_19480_19491[(2)] = inst_19468);

(statearr_19480_19491[(1)] = (3));


return cljs.core.cst$kw$recur;
} else {
if((state_val_19473 === (7))){
var inst_19446 = (state_19472[(7)]);
var inst_19453 = (state_19472[(9)]);
var inst_19447 = (state_19472[(8)]);
var inst_19460 = (state_19472[(2)]);
var inst_19461 = cljs.core.prn.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([inst_19453], 0));
var inst_19462 = cljs.core.prn.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([inst_19460], 0));
var inst_19463 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(inst_19446,inst_19453,inst_19460);
var inst_19464 = cljs.core.rest(inst_19447);
var inst_19446__$1 = inst_19463;
var inst_19447__$1 = inst_19464;
var state_19472__$1 = (function (){var statearr_19481 = state_19472;
(statearr_19481[(7)] = inst_19446__$1);

(statearr_19481[(8)] = inst_19447__$1);

(statearr_19481[(10)] = inst_19461);

(statearr_19481[(11)] = inst_19462);

return statearr_19481;
})();
var statearr_19482_19492 = state_19472__$1;
(statearr_19482_19492[(2)] = null);

(statearr_19482_19492[(1)] = (2));


return cljs.core.cst$kw$recur;
} else {
return null;
}
}
}
}
}
}
}
});})(c__15583__auto__))
;
return ((function (switch__15400__auto__,c__15583__auto__){
return (function() {
var mecca$music$load_samples_$_state_machine__15401__auto__ = null;
var mecca$music$load_samples_$_state_machine__15401__auto____0 = (function (){
var statearr_19483 = [null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_19483[(0)] = mecca$music$load_samples_$_state_machine__15401__auto__);

(statearr_19483[(1)] = (1));

return statearr_19483;
});
var mecca$music$load_samples_$_state_machine__15401__auto____1 = (function (state_19472){
while(true){
var ret_value__15402__auto__ = (function (){try{while(true){
var result__15403__auto__ = switch__15400__auto__(state_19472);
if(cljs.core.keyword_identical_QMARK_(result__15403__auto__,cljs.core.cst$kw$recur)){
continue;
} else {
return result__15403__auto__;
}
break;
}
}catch (e19484){if((e19484 instanceof Object)){
var ex__15404__auto__ = e19484;
var statearr_19485_19493 = state_19472;
(statearr_19485_19493[(5)] = ex__15404__auto__);


cljs.core.async.impl.ioc_helpers.process_exception(state_19472);

return cljs.core.cst$kw$recur;
} else {
throw e19484;

}
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__15402__auto__,cljs.core.cst$kw$recur)){
var G__19494 = state_19472;
state_19472 = G__19494;
continue;
} else {
return ret_value__15402__auto__;
}
break;
}
});
mecca$music$load_samples_$_state_machine__15401__auto__ = function(state_19472){
switch(arguments.length){
case 0:
return mecca$music$load_samples_$_state_machine__15401__auto____0.call(this);
case 1:
return mecca$music$load_samples_$_state_machine__15401__auto____1.call(this,state_19472);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
mecca$music$load_samples_$_state_machine__15401__auto__.cljs$core$IFn$_invoke$arity$0 = mecca$music$load_samples_$_state_machine__15401__auto____0;
mecca$music$load_samples_$_state_machine__15401__auto__.cljs$core$IFn$_invoke$arity$1 = mecca$music$load_samples_$_state_machine__15401__auto____1;
return mecca$music$load_samples_$_state_machine__15401__auto__;
})()
;})(switch__15400__auto__,c__15583__auto__))
})();
var state__15585__auto__ = (function (){var statearr_19486 = (f__15584__auto__.cljs$core$IFn$_invoke$arity$0 ? f__15584__auto__.cljs$core$IFn$_invoke$arity$0() : f__15584__auto__.call(null));
(statearr_19486[(6)] = c__15583__auto__);

return statearr_19486;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__15585__auto__);
});})(c__15583__auto__))
);

return c__15583__auto__;
});
if((typeof mecca !== 'undefined') && (typeof mecca.music !== 'undefined') && (typeof mecca.music.loading_samples !== 'undefined')){
} else {
mecca.music.loading_samples = (function (){var c__15583__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run(((function (c__15583__auto__){
return (function (){
var f__15584__auto__ = (function (){var switch__15400__auto__ = ((function (c__15583__auto__){
return (function (state_19501){
var state_val_19502 = (state_19501[(1)]);
if((state_val_19502 === (1))){
var inst_19495 = mecca.music.load_samples();
var state_19501__$1 = state_19501;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_19501__$1,(2),inst_19495);
} else {
if((state_val_19502 === (2))){
var inst_19497 = (state_19501[(2)]);
var inst_19498 = mecca.music.samples = inst_19497;
var inst_19499 = cljs.core.prn.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["Samples loaded"], 0));
var state_19501__$1 = (function (){var statearr_19503 = state_19501;
(statearr_19503[(7)] = inst_19498);

return statearr_19503;
})();
return cljs.core.async.impl.ioc_helpers.return_chan(state_19501__$1,inst_19499);
} else {
return null;
}
}
});})(c__15583__auto__))
;
return ((function (switch__15400__auto__,c__15583__auto__){
return (function() {
var mecca$music$state_machine__15401__auto__ = null;
var mecca$music$state_machine__15401__auto____0 = (function (){
var statearr_19504 = [null,null,null,null,null,null,null,null];
(statearr_19504[(0)] = mecca$music$state_machine__15401__auto__);

(statearr_19504[(1)] = (1));

return statearr_19504;
});
var mecca$music$state_machine__15401__auto____1 = (function (state_19501){
while(true){
var ret_value__15402__auto__ = (function (){try{while(true){
var result__15403__auto__ = switch__15400__auto__(state_19501);
if(cljs.core.keyword_identical_QMARK_(result__15403__auto__,cljs.core.cst$kw$recur)){
continue;
} else {
return result__15403__auto__;
}
break;
}
}catch (e19505){if((e19505 instanceof Object)){
var ex__15404__auto__ = e19505;
var statearr_19506_19508 = state_19501;
(statearr_19506_19508[(5)] = ex__15404__auto__);


cljs.core.async.impl.ioc_helpers.process_exception(state_19501);

return cljs.core.cst$kw$recur;
} else {
throw e19505;

}
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__15402__auto__,cljs.core.cst$kw$recur)){
var G__19509 = state_19501;
state_19501 = G__19509;
continue;
} else {
return ret_value__15402__auto__;
}
break;
}
});
mecca$music$state_machine__15401__auto__ = function(state_19501){
switch(arguments.length){
case 0:
return mecca$music$state_machine__15401__auto____0.call(this);
case 1:
return mecca$music$state_machine__15401__auto____1.call(this,state_19501);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
mecca$music$state_machine__15401__auto__.cljs$core$IFn$_invoke$arity$0 = mecca$music$state_machine__15401__auto____0;
mecca$music$state_machine__15401__auto__.cljs$core$IFn$_invoke$arity$1 = mecca$music$state_machine__15401__auto____1;
return mecca$music$state_machine__15401__auto__;
})()
;})(switch__15400__auto__,c__15583__auto__))
})();
var state__15585__auto__ = (function (){var statearr_19507 = (f__15584__auto__.cljs$core$IFn$_invoke$arity$0 ? f__15584__auto__.cljs$core$IFn$_invoke$arity$0() : f__15584__auto__.call(null));
(statearr_19507[(6)] = c__15583__auto__);

return statearr_19507;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__15585__auto__);
});})(c__15583__auto__))
);

return c__15583__auto__;
})();
}
mecca.music.pitch__GT_rate = (function mecca$music$pitch__GT_rate(midi_num){
var G__19510 = midi_num;
switch (G__19510) {
case (56):
return 0.5297315471796479;

break;
case 56.5:
return 0.5297315471796479;

break;
case (57):
return 0.5612310241546867;

break;
case 57.5:
return 0.5946035575013607;

break;
case (58):
return 0.6299605249474368;

break;
case 58.5:
return 0.6674199270850174;

break;
case (59):
return 0.7071067811865477;

break;
case 59.5:
return 0.7071067811865477;

break;
case (60):
return 0.7491535384383409;

break;
case 60.5:
return 0.7937005259840998;

break;
case (61):
return 0.8408964152537146;

break;
case 61.5:
return 0.8908987181403394;

break;
case (62):
return 0.9438743126816935;

break;
case 62.5:
return (1);

break;
case (63):
return 1.0594630943592953;

break;
case 63.5:
return 1.122462048309373;

break;
case (64):
return 1.122462048309373;

break;
case 64.5:
return 1.1892071150027212;

break;
case (65):
return 1.2599210498948734;

break;
case 65.5:
return 1.3348398541700346;

break;
case (66):
return 1.4142135623730954;

break;
case 66.5:
return 1.498307076876682;

break;
case (67):
return 1.498307076876682;

break;
case 67.5:
return 1.5874010519682;

break;
case (68):
return 1.6817928305074297;

break;
case 68.5:
return 1.7817974362806792;

break;
case (69):
return 1.8877486253633877;

break;
case 69.5:
return (2);

break;
case (70):
return 2.1189261887185906;

break;
case 70.5:
return 2.244924096618746;

break;
default:
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__19510)].join('')));

}
});
mecca.music.play_sample = (function mecca$music$play_sample(instrument,pitch){
var context = mecca.music.audiocontext;
var audio_buffer = cljs.core.cst$kw$decoded_DASH_buffer.cljs$core$IFn$_invoke$arity$1(cljs.core.get.cljs$core$IFn$_invoke$arity$2(mecca.music.samples,instrument));
var sample_source = cljs.core.deref(context).createBufferSource();
sample_source.buffer = audio_buffer;

sample_source.playbackRate.setValueAtTime(mecca.music.pitch__GT_rate(pitch),cljs.core.deref(context).currentTime);

sample_source.connect(cljs.core.deref(context).destination);

sample_source.start();

return sample_source;
});
mecca.music.play_at = (function mecca$music$play_at(instrument,pitch,time){
var context = mecca.music.audiocontext;
var audio_buffer = cljs.core.cst$kw$decoded_DASH_buffer.cljs$core$IFn$_invoke$arity$1(cljs.core.get.cljs$core$IFn$_invoke$arity$2(mecca.music.samples,instrument));
var sample_source = cljs.core.deref(context).createBufferSource();
sample_source.buffer = audio_buffer;

sample_source.playbackRate.setValueAtTime(mecca.music.pitch__GT_rate(pitch),time);

sample_source.connect(cljs.core.deref(context).destination);

sample_source.start(time);

return sample_source;
});
mecca.music.play_song_BANG_ = (function mecca$music$play_song_BANG_(){
var notes = (function (){var G__19512 = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$notes], null);
return (re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1 ? re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(G__19512) : re_frame.core.subscribe.call(null,G__19512));
})();
var now = cljs.core.deref(mecca.music.audiocontext).currentTime;
var tempo = (function (){var G__19513 = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$tempo], null);
return (re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1 ? re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(G__19513) : re_frame.core.subscribe.call(null,G__19513));
})();
var G__19514_19523 = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$reset_DASH_position], null);
(re_frame.core.dispatch.cljs$core$IFn$_invoke$arity$1 ? re_frame.core.dispatch.cljs$core$IFn$_invoke$arity$1(G__19514_19523) : re_frame.core.dispatch.call(null,G__19514_19523));

return cljs.core.doall.cljs$core$IFn$_invoke$arity$1((function (){var iter__4324__auto__ = ((function (notes,now,tempo){
return (function mecca$music$play_song_BANG__$_iter__19515(s__19516){
return (new cljs.core.LazySeq(null,((function (notes,now,tempo){
return (function (){
var s__19516__$1 = s__19516;
while(true){
var temp__5457__auto__ = cljs.core.seq(s__19516__$1);
if(temp__5457__auto__){
var s__19516__$2 = temp__5457__auto__;
if(cljs.core.chunked_seq_QMARK_(s__19516__$2)){
var c__4322__auto__ = cljs.core.chunk_first(s__19516__$2);
var size__4323__auto__ = cljs.core.count(c__4322__auto__);
var b__19518 = cljs.core.chunk_buffer(size__4323__auto__);
if((function (){var i__19517 = (0);
while(true){
if((i__19517 < size__4323__auto__)){
var map__19519 = cljs.core._nth.cljs$core$IFn$_invoke$arity$2(c__4322__auto__,i__19517);
var map__19519__$1 = ((((!((map__19519 == null)))?(((((map__19519.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__19519.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__19519):map__19519);
var time = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__19519__$1,cljs.core.cst$kw$time);
var instrument = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__19519__$1,cljs.core.cst$kw$instrument);
var pitch = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__19519__$1,cljs.core.cst$kw$pitch);
cljs.core.chunk_append(b__19518,mecca.music.play_at(instrument,pitch,(now + (((60) / cljs.core.deref(tempo)) * time))));

var G__19524 = (i__19517 + (1));
i__19517 = G__19524;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__19518),mecca$music$play_song_BANG__$_iter__19515(cljs.core.chunk_rest(s__19516__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__19518),null);
}
} else {
var map__19521 = cljs.core.first(s__19516__$2);
var map__19521__$1 = ((((!((map__19521 == null)))?(((((map__19521.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__19521.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__19521):map__19521);
var time = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__19521__$1,cljs.core.cst$kw$time);
var instrument = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__19521__$1,cljs.core.cst$kw$instrument);
var pitch = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__19521__$1,cljs.core.cst$kw$pitch);
return cljs.core.cons(mecca.music.play_at(instrument,pitch,(now + (((60) / cljs.core.deref(tempo)) * time))),mecca$music$play_song_BANG__$_iter__19515(cljs.core.rest(s__19516__$2)));
}
} else {
return null;
}
break;
}
});})(notes,now,tempo))
,null,null));
});})(notes,now,tempo))
;
return iter__4324__auto__(cljs.core.deref(notes));
})());
});
