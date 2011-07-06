(ns clj-cloudq.routes
  (:use compojure.core)
  (:require [compojure.route :as route]
            [clj-json.core :as json]))

(defn json-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json"}
   :body (json/generate-string data)})

(defroutes main-routes

;;POST /:queue
;;When a client requests a POST for a queue name the server needs to
;;first make sure that JOB is valid, if the job is not valid then
;;return a response message. For our ruby and node implementations we
;;use MongoDb and create a collection called jobs and the documents in
;;the jobs collection contain an attribute called :queue
  (POST "/:queue-name" [queue-name klass args]
        (println (str queue-name "|" klass "|" args))
        (json-response {:name queue-name
                        :klass klass
                        :args args}))
  
;;GET /:queue
;;When a client performs a get request, they are requesting an items
;;from the :queue, the server should find a JOB with a status of :new
;;and update that item to a status of :reserved then respond to the
;;client with the JOB. If there are no JOBS in a new status for that
;;queue then the server should return an empty response.
  (GET "/:queue-name" [queue-name]
        (println queue-name))

  
;;DELETE /:queue/:id
;;When a client requests a DELETE JOB request, the server needs to
;;locate the JOB and modify the status of the job to :deleted, and
;;return a success response, if it can't find the job then return an
;;error response or empty response.
  (DELETE "/:queue-name/:id" [queue-name id]
          (println (str queue-name "|" id)))

  
  (route/resources "/")
  (route/not-found "Page not found"))

