(ns clj-cloudq.core
  (:use ring.middleware.json-params)
  (:require [compojure.handler :as handler]
            [clj-cloudq.routes :as routes]
            [clj-cloudq.queue :as q]
            [ring.adapter.jetty :as jetty]))

(def app
     (-> routes/main-routes
         wrap-json-params
         handler/api))

(defn start [port]
  (do
    (q/connect)
    (jetty/run-jetty (var app) {:port (or port 8080) :join? false})))

(defn -main []
  (let [port (Integer/parseInt (System/getenv "PORT"))]
    (q/connect)
    (start port)))
