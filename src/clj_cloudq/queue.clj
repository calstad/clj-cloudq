(ns clj-cloudq.queue
  (:import [org.bson.types ObjectId])
  (:use somnium.congomongo)
  (:require [clj-cloudq.config :as conf]))

(defn split-mongo-url [url]
  "Parses mongodb url from heroku, eg. mongodb://user:pass@localhost:1234/db"
  (let [matcher (re-matcher #"^.*://(.*?):(.*?)@(.*?):(\d+)/(.*)$" url)]
    (when (.find matcher)
      (zipmap [:match :user :pass :host :port :db] (re-groups matcher)))))

(defn get-config []
  "Returns the config defined in the MONGOHQ_URL environment variable or the config namespace if the MongoHQ url does not exiest."
  (let [mongo-url (get (System/getenv) "MONGOHQ_URL")]
    (if mongo-url
      (split-mongo-url mongo-url)
      conf/mongodb)))

(defn connect []
  "Connects to MongoDB"
  (let [config (get-config)]
    (mongo! :db (:db config) :host (:host config) :port (Integer. (:port config)))
    (when (:user config)
      (authenticate (:user config) (:pass config)))))

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
    (if job
      (do
        (update! queue-name job (merge job {:status "reserved"}))
        {:klass (:klass job) :id (str (:_id job)) :args (:args job)})
      {:status "empty"})))

;;The worker verifies that it go the JOB, then submits a DELETE
;;request to the Cloudq Server. This changes the status of the JOB
;;from :reserved to :deleted.
(defn delete-job
  [queue-name id]
  (destroy! queue-name {:_id (org.bson.types.ObjectId. id)}))

