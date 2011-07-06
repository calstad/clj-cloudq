(defproject clj-cloudq "1.0.0-SNAPSHOT"
  :description "A clojure implementation of the cloudq server."
  :resources-path "resources"
  :uberjar-name "cloudq.jar"
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [aleph "0.2.0-alpha3-SNAPSHOT"]
                 [net.cgrand/moustache "1.0.0"]
                 [congomongo "0.1.5-SNAPSHOT"]]
  :dev-dependencies [[swank-clojure "1.4.0-SNAPSHOT"]])
