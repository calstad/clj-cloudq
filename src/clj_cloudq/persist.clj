(ns clj-cloudq.persist
  (:use somnium.congomongo)
  (:require [clj-cloudq.config :as config]))

(defn split-mongo-url [url]
  "Parses mongodb url from heroku, eg. mongodb://user:pass@localhost:1234/db"
  (let [matcher (re-matcher #"^.*://(.*?):(.*?)@(.*?):(\d+)/(.*)$" url)] ;; Setup the regex.
    (when (.find matcher)
      (zipmap [:match :user :pass :host :port :db] (re-groups matcher)))))

(defn connect []
  "Connects to the MongoDB defined in the MONGOHQ_URL environment variable."
  (let [mongo-url (get (System/getenv) "MONGOHQ_URL")
        config    (split-mongo-url mongo-url)]
    (mongo! :db (:db config) :host (:host config) :port (Integer. (:port config))) ;; Setup global mongo.
    (authenticate (:user config) (:pass config))))

(defn jobs-collection []
  (:collection config/mongodb))

(defn find-and-reserve-job [queue-name]
  (fetch-and-modify (jobs-collection) :where {:queue queue-name :workflow_state "queued"} :update {:workflow_state "reserved"} :return-new? true))
