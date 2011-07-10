(ns clj-cloudq.core
  (:use ring.middleware.json-params)
  (:require [compojure.handler :as handler]
            [clj-cloudq.routes :as routes]
            [clj-cloudq.queue :as q]
            [clj-cloudq.config :as config]
            [ring.adapter.jetty :as jetty]))

(def app
  (-> routes/main-routes
      wrap-json-params
      handler/api))

(defn get-server-port []
  (let [port (System/getenv "PORT")]
    (if port (Integer/parseInt port) config/server-port)))

(defn start [port]
  (do
    (q/connect)
    (jetty/run-jetty (var app) {:port port :join? false})))

(defn -main []
  (q/connect)
  (start (get-server-port)))
