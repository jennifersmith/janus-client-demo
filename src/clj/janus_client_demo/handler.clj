(ns janus-client-demo.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [hiccup.page :refer [html5 include-js include-css]]
            [hiccup.bootstrap.page :refer [include-bootstrap]]
            [hiccup.bootstrap.middleware :refer [wrap-bootstrap-resources]]
            [ring.util.response :as resp]))

(defn layout [& content]
  (html5
    [:head
     [:title "Hello World"]
     [:link {:rel "stylesheet"}]
     (include-css "http://fonts.googleapis.com/css?family=Open+Sans:400italic,700italic,400,700")
     (include-css "/bootstrap/css/bootstrap.css")
     (include-js "http://codeorigin.jquery.com/jquery-2.0.3.min.js")
     (include-js "http://cdnjs.cloudflare.com/ajax/libs/d3/3.3.9/d3.min.js")
     (include-js  "/bootstrap/js/bootstrap.min.js")
     (include-css "bootstrap/css/font-awesome.css")

     ]
    [:body
     [:div {:class "container"} content]
          (include-js "generated/dev.js")]))

(defn weather []
  [:div {:class "jumbotron" }
   [:div {:class "container"}
    [:h2 {:class "jumbotron-title"} "Weather Service"]
    [:div {:id :weather-container}]
    ]]
)
(defroutes app-routes
  (GET "/" [] (layout (weather)))
  (route/resources "/")
  (route/resources "/bootstrap" {:root "public/assets"}) ;; custom theme
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (handler/site)))
