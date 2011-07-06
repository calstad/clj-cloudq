(ns clj-cloudq.core
  (:use ring.middleware.json-params)
  (:require [compojure.handler :as handler]
            [clj-cloudq.routes :as routes]))

(def app
     (-> routes/main-routes
         wrap-json-params
         handler/api))

