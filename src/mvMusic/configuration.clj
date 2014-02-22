(ns mvMusic.configuration
  (:use [mvMusic.global])
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]))

(def cfg-name ".mvMusic-cfg.edn")

(def cfg-path (str (System/getProperty "user.home") "/" cfg-name))

(defn cfg-exists? []
  "Return true if file given by cfg-loc exists. Otherwise return false"
  (.exists (io/as-file cfg-path)))

(defn load-cfg
  []
  "load config into cfg-map from location specified by cfg-loc. Create file if 
  it doesn't already exist."
  (if (cfg-exists?)
    (with-open [rdr (java.io.PushbackReader. (io/reader cfg-path))]
       (intern 'mvMusic.global 'cfg-map (merge cfg-map (edn/read rdr))))
    ((spit cfg-path (str cfg-map)))))

(load-cfg)
