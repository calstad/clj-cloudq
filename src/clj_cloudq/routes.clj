(ns clj-cloudq.routes
  (:use compojure.core)
  (:require [compojure.route :as route]
            [clj-json.core :as json]
            [clj-cloudq.queue :as q]))

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
  (POST "/:queue-name" [queue-name job]
        (cq/add-job queue-name job))

  ;;GET /:queue
  ;;When a client performs a get request, they are requesting an items
  ;;from the :queue, the server should find a JOB with a status of :new
  ;;and update that item to a status of :reserved then respond to the
  ;;client with the JOB. If there are no JOBS in a new status for that
  ;;queue then the server should return an empty response.
  (GET "/:queue-name" [queue-name]
       (json-response (cq/get-job)))


  ;;DELETE /:queue/:id
  ;;When a client requests a DELETE JOB request, the server needs to
  ;;locate the JOB and modify the status of the job to :deleted, and
  ;;return a success response, if it can't find the job then return an
  ;;error response or empty response.
  (DELETE "/:queue-name/:id" [queue-name id]
          (println (str queue-name "|" id)))


=======
;;POST /:queue
;;When a client requests a POST for a queue name the server needs to
;;first make sure that JOB is valid, if the job is not valid then
;;return a response message. For our ruby and node implementations we
;;use MongoDb and create a collection called jobs and the documents in
;;the jobs collection contain an attribute called :queue
  (POST "/:queue-name" [queue-name klass args]
        (q/add-job queue-name {:klass klass :args args})
        (json-response {:status "success"}))
  
;;GET /:queue
;;When a client performs a get request, they are requesting an items
;;from the :queue, the server should find a JOB with a status of :new
;;and update that item to a status of :reserved then respond to the
;;client with the JOB. If there are no JOBS in a new status for that
;;queue then the server should return an empty response.
  (GET "/:queue-name" [queue-name]
       (let [job (q/get-job queue-name)
             id (str (:_id job))]
         (json-response {:klass (:klass job)
                         :id id
                         :args (:args job)})))
        

  
;;DELETE /:queue/:id
;;When a client requests a DELETE JOB request, the server needs to
;;locate the JOB and modify the status of the job to :deleted, and
;;return a success response, if it can't find the job then return an
;;error response or empty response.
  (DELETE "/:queue-name/:id" [queue-name id]
          (q/delete-job queue-name id)
          (json-response {:status "success"}))
  
  (route/resources "/")
  (route/not-found "Page not found"))

