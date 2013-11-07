(ns cljs.weather
  (:require [domina :refer [ by-id log append! destroy-children! set-attr! remove-attr! add-class! remove-class! destroy!]]
            [domina.css :refer [sel]]
            [domina.events :refer [listen! target]]
            [hiccups.runtime :as hiccupsrt]
            [ajax.core :refer [GET POST]]
            )
  (:require-macros [hiccups.core :as hiccups]))

(def color-from-temp
  (->
   (.-scale js/d3)
   (.quantize)
   (.domain (clj->js [-5 45]))
   (.range (clj->js (reverse ["#b2182b","#d6604d","#f4a582","#fddbc7","#f7f7f7","#d1e5f0","#92c5de","#4393c3","#2166ac"]) ))))

;;

(def endpoint "http://localhost:8787/cities")

(hiccups/defhtml weather-template []      
  [:div
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
  [:div {:class "row col-md-offset-2 col-md-8 alert alert-block fade in out" :style (str "font-weight:bold; color:#000000; background-color:" (color-from-temp (get city "temp")))}
   [:div {:class :col-md-10}
    [:h4 (. (get city "name") toUpperCase)]]
   [:div {:class :col-md-2}
    [:h4 (get city "temp") " ºC"]]])

(hiccups/defhtml cities-template [cities]
  (vec
   (concat [:div {:class "row fade in out hide"}]
           (map city-template cities)))
)

(defn show-loading! [target] 
  (append! target (loading-template)))

(defn create-error-handler [target] 
  (fn [resp]
    (destroy-children! target)
    (append! target (error-template))))

(defn load-weather [interval]
  (let [results (by-id "weather-results")
        success (fn [r] 
                  (add-class! (sel results "div") "hide old")
                  (append! results (cities-template (get r "cities")))
                  (destroy! (sel results ".old"))
                  (remove-class! (sel results "div") "hide")
                  (js/setTimeout #(load-weather interval) interval)
                  )]
    (GET endpoint {:handler success :error-handler (create-error-handler results)})))

(defn init [root]
  (when-not (nil? root)
    (destroy-children! root)
    (append! root (weather-template))
    (load-weather 3000)))

(init (by-id "weather-container"))
