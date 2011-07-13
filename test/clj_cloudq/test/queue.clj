(ns clj-cloudq.test.queue
  (:use [clj-cloudq.queue])
  (:use [clojure.test]))

(deftest splits-mongohq-url-into-connection-paramater-map
  (let [params-map (split-mongo-url "mongodb://username:password@localhost:1234/database")]
      (is (= "username"  (:user params-map)))
      (is (= "password"  (:pass params-map)))
      (is (= "localhost" (:host params-map)))
      (is (= "1234"      (:port params-map)))
      (is (= "database"  (:db params-map)))))

