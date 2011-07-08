(ns clj-cloudq.queue
  (:use somnium.congomongo)
  (:require [clj-cloudq.persist :as persist]))

;;An Application Posts a JOB to a Cloudq Server Queue, that JOB is set to a state of :queued.
(defn add-job
  [queue-name payload]
  )

;;Then a Worker Requests (GET) a JOB from the Cloudq Server Queue, the
;;server sets the state of that JOB to :reserved and hands it to the
;;worker.
(defn get-job [queue-name]
  (persist/find-and-reserve-job queuename))

;;The worker verifies that it go the JOB, then submits a DELETE
;;request to the Cloudq Server. This changes the status of the JOB
;;from :reserved to :deleted.
(defn delete-job
  [queue-name id]
  )
