(ns cljs.weather
  (:require [domina :refer [ by-id log append! destroy-children! set-attr! remove-attr!]]
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
    [:div {:class "container" :id :weather-results}]]])

(hiccups/defhtml loading-template []      
  [:div {:class "fade in out alert alert-warning"} "Loading... be patient"])

(hiccups/defhtml error-template []      
  [:div {:class "alert alert-block alert-danger"} 
   [:button {:type :button :class :close :data-dismiss :alert}]
   [:h4 {:class "alert-heading"} "Oh no something happened!"]
   [:p "It's probably worth reloading the page as the author did not do great error handling."]])


(hiccups/defhtml city-template [city]
  [:div {:class "row col-md-offset-2 col-md-8 alert alert-block alert-info"}
   [:div {:class :col-md-10}
    [:h4 (get city "name")]]
   [:div {:class :col-md-2}
    [:h4 (get city "temp") " ºC"]]])

(hiccups/defhtml cities-template [cities]
  (vec
   (concat [:div {:class "row"}]
           (map city-template cities)))
)

(defn show-loading! [target] 
  (append! target (loading-template)))

(defn create-error-handler [target] 
  (fn [resp]
    (destroy-children! target)
    (append! target (error-template))))

(defn load-weather [event]
  (let [results (by-id "weather-results")
        button (target event)
        success (fn [r] 
                  (remove-attr! button :disabled)
                  (destroy-children! results)
                  (append! results (cities-template (get r "cities")))
                  )]
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
