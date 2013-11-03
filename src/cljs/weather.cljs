(ns cljs.weather
  (:require [domina :refer [ by-id log append! destroy-children! set-attr!]]
            [domina.css :refer [sel]]
            [domina.events :refer [listen! target]]
            [hiccups.runtime :as hiccupsrt]
            [ajax.core :refer [GET POST]]
            )
  (:require-macros [hiccups.core :as hiccups]))

(def endpoint "http://localhost:8787/cities")

(hiccups/defhtml weather-template []      
  [:div
    [:button {:class "go-button btn btn-primary btn-lg btn-block"}
      "GO"]
   [:div {:class :container}
    [:h3 "What's the weather like?"]
    [:div {:id :weather-results}]]])

(hiccups/defhtml loading-template []      
  [:div {:class "alert alert-info"} "Loading... be patient"])

(hiccups/defhtml error-template []      
  [:div {:class "alert alert-block alert-danger fade in"} 
   [:button {:type :button :class :close :data-dismiss :alert}]
   [:h4 {:class "alert-heading"} "Oh no something happened!"]
   [:p "It's probably worth reloading the page as the author did not do great error handling."]])

(defn show-loading! [target] 
  (append! target (loading-template)))

(defn create-error-handler [target] 
  (fn [resp]
    (destroy-children! target)
    (append! target (error-template))))

(defn load-weather [event]
  (let [results (by-id "weather-results")
        button (target event)
        success (fn [r] (log "TODO RENDER RESULTS" r))]
    (destroy-children! results)
    (show-loading! results) 
    (set-attr! button :disabled true)
    (GET endpoint {:handler success :error-handler (create-error-handler results)})))

(defn init [root]
  (when-not (nil? root)
    (destroy-children! root)
    (append! root (weather-template))
    (listen! (sel root ".go-button") :click load-weather)))

(init (by-id "weather-container"))
