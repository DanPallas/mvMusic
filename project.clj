(defproject mvMusic "0.1.0-SNAPSHOT"
  :description "A server application for converting and downloading music with a web front end"
  :url "https://github.com/DanPallas/mvMusic.git"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [hiccup "1.0.4"]]
  :plugins [[lein-ring "0.8.7"]]
  :ring {:handler mvMusic.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.5"]]}})
