(defproject clj-cloudq "1.0.0-SNAPSHOT"
  :description "A clojure implementation of the cloudq server."
  :resources-path "resources"
  :uberjar-name "cloudq.jar"
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [compojure "0.6.4"]                 
                 [congomongo "0.1.5-SNAPSHOT"]
                 [ring-json-params "0.1.3"]
                 [clj-json "0.3.2"]]
  :dev-dependencies [[swank-clojure "1.4.0-SNAPSHOT"]
                     [lein-ring "0.4.5"]]
  :ring {:handler clj-cloudq.core/app})
