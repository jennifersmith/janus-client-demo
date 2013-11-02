(ns cljs.weather
  (:require [domina :refer [ by-id log]]
            [domina.events :refer [listen!]]))

(listen!
:click (fn [evt] (js/console.log "button clicked!")))
