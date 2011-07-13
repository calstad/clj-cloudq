(ns clj-cloudq.test.routes
  (:use [clj-cloudq.routes])
  (:use [clojure.test]))

(defn request [resource web-app & params]
  (web-app {:request-method :get :uri resource :params (first params)}))

(deftest test-routes
  (is (= 200 (:status (request "/" main-routes))))
  (is (= "Hello from CloudQ" (:body (request "/" main-routes)))))

(deftest json-responder-default-status
  (is (= 200 (:status (json-response {:foo "bar"})))))

(deftest json-responder-provided-status
  (is (= 401 (:status (json-response {:foo "bar"} 401)))))

(deftest json-responder-content-type
  (is (= "application/json" (get (:headers (json-response {:foo "bar"})) "Content-Type"))))
