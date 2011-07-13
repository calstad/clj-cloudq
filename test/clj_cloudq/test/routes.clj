(ns clj-cloudq.test.routes
  (:use clj-cloudq.routes
        clojure.test
        ring.mock.request
        clojure.contrib.mock.test-adapter)
  (:require [clj-cloudq.queue :as q]))

(defn cloudq-request
  "Wrapper around ring mock request for convienence"
  [method resource & job-params]
  (main-routes (-> (request method resource) (assoc :params {"job" job-params}))))

(deftest root-url
  (let [root-response (cloudq-request :get "/")]
    (is (= 200 (:status root-response)))
    (is (= "Hello from CloudQ" (:body root-response)))))

(deftest post-job
  (expect [q/add-job (times once (has-args ["test_queue" {:klass "TestClass" :args [{:args1 "test_args"}]}]))]
          (cloudq-request :post "/test_queue" {:klass "TestClass" :args [{:args1 "test_args"}]})))

(deftest json-responder-default-status
  (is (= 200 (:status (json-response {:foo "bar"})))))

(deftest json-responder-provided-status
  (is (= 401 (:status (json-response {:foo "bar"} 401)))))

(deftest json-responder-content-type
  (is (= "application/json" (get (:headers (json-response {:foo "bar"})) "Content-Type"))))


