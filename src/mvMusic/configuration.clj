(ns mvMusic.configuration
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]))

; --------------- Defines -----------------
(def cfg-name ".mvMusic-cfg.edn")

; ---------------- end defines --------------

(def cfg-path (str (System/getProperty "user.home") "/" cfg-name))

(def cfg-map {:music-folder (str (System/getProperty "user.home") "/" "Music")
              :temp-folder 
                (str (System/getProperty "user.home") "/" "Music/mvMusic-tmp")})

(defn cfg-exists? []
  "Return true if file given by cfg-loc exists. Otherwise return false"
  (.exists (io/as-file cfg-path)))

(defn load-cfg
  []
  "load config into cfg-map from location specified by cfg-loc. Create file if 
  it doesn't already exist."
  (if (cfg-exists?)
    (with-open [rdr (java.io.PushbackReader. (io/reader cfg-path))]
       (def cfg-map (merge cfg-map (edn/read rdr))))
    ((spit cfg-path (str cfg-map)))))

