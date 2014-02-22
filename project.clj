(defproject mvMusic "0.1.0-SNAPSHOT"
  :description "A server application for converting and downloading music with a web front end"
  :url "https://github.com/DanPallas/mvMusic.git"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [hiccup "1.0.4"]
                 [org/jaudiotagger "2.0.3"]
                 [org.xerial/sqlite-jdbc "3.7.15-M1"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [korma "0.3.0-RC6"]
               ;  [log4j/log4j "1.2.17"]
                 [org.clojure/tools.logging "0.2.6"]]
  :plugins [[lein-ring "0.8.7"]]
  :ring {:init mvMusic.configuration/load-cfg
         :handler mvMusic.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.5"]
                        [midje "1.6.0"]]}})
