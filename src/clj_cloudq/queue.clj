(ns clj-cloudq.queue
  (:import [org.bson.types ObjectId])
  (:use somnium.congomongo)
  (:require [clj-cloudq.config :as config]))

(defn split-mongo-url [url]
  "Parses mongodb url from heroku, eg. mongodb://user:pass@localhost:1234/db"
  (let [matcher (re-matcher #"^.*://(.*?):(.*?)@(.*?):(\d+)/(.*)$" url)]
    (when (.find matcher)
      (zipmap [:match :user :pass :host :port :db] (re-groups matcher)))))

(defn connect []
  "Connects to the MongoDB defined in the MONGOHQ_URL environment variable."
  (let [mongo-url (get (System/getenv) "MONGOHQ_URL")
        config    (split-mongo-url mongo-url)]
    (mongo! :db (:db config) :host (:host config) :port (Integer. (:port config)))
    (authenticate (:user config) (:pass config))))

;;An Application Posts a JOB to a Cloudq Server Queue, that JOB is set to a state of :new.
(defn add-job
  [queue-name payload]
  (insert! queue-name payload))

;;Then a Worker Requests (GET) a JOB from the Cloudq Server Queue, the
;;server sets the state of that JOB to :reserved and hands it to the
;;worker.
(defn get-job
  [queue-name]
  (let [job (fetch-one queue-name :where {:status {:$ne "reserved"}})]
    (update! queue-name job (merge job {:status "reserved"}))
    job))

;;The worker verifies that it go the JOB, then submits a DELETE
;;request to the Cloudq Server. This changes the status of the JOB
;;from :reserved to :deleted.
(defn delete-job
  [queue-name id]
  (destroy! queue-name {:_id (org.bson.types.ObjectId. id)}))

