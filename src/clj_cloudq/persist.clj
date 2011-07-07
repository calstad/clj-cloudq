(ns clj-cloudq.persist
   (:use somnium.congomongo)
  (:require [clj-cloudq.config :as config]))

(defn connect []
  (let [conf config/mongodb]
    (mongo! :db (:database conf) :host (:host conf) :port (Integer. (:port conf)))
    (if (:user conf)
      (authenticate (:user conf) (:password conf)))))



