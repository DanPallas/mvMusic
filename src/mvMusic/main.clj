(ns mvMusic.main
  (:use [ring.adapter.jetty])
  (:require [mvMusic.handler :refer [app]])
  (:gen-class :main true))

(defn -main
  [& port]
  (let
    [port (if port (first port) 80)]
    (run-jetty app {:port port})))
