(defproject mvMusic "0.1.0-SNAPSHOT"
  :description "A server application for converting and downloading music with a web front end"
  :url "https://github.com/DanPallas/mvMusic.git"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.5"]
                 [ring/ring-json "0.3.1"]
                 [ring/ring-jetty-adapter "1.3.0"]
                 [org.clojure/java.jdbc "0.3.3"]
                 [com.h2database/h2 "1.4.178"]
                 [org.clojure/tools.logging "0.2.6"] ]
  :plugins [[lein-ring "0.8.10"]
            [lein-pdo "0.1.1"] ]
  :ring {:init mvMusic.configuration/load-cfg
         :handler mvMusic.handler/app}
  :profiles {:dev {:dependencies [[ring-mock "0.1.5"]
                                  [midje "1.6.0"]]
                   :prep-tasks ["javac" "compile"]}}
  :source-paths ["src"]
  :aot :all
  :omit-source true
  :main mvMusic.main)
