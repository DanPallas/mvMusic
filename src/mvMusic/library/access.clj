(ns mvMusic.library.access
  (:require [mvMusic.log :as log]
            [clojure.java.io :refer [as-file]]
            [clojure.string :as s]
            [clojure.java.jdbc :as j]))

(defmacro debug^:private [label i]
  `(let [o# ~i
         _# (println (str "Debug " ~label ": " o#))]
     o#))

(defn- key->column
  [k]
  (keyword (s/replace (s/upper-case (name k)) #"-" "_")))

(defn- keys->columns
  [m]
  (reduce 
    (fn [m [k v]] (assoc m (key->column k) v))
    {}
    (seq m)))

(defn- assoc-file-folder
  [song]
  (let [file (as-file (as-file (:file song)))]
    (assoc song
           :folder (.getParent file)
           :filename (.getName file))))

(defn- remove-articles
  [string]
  (if (nil? string) 
    nil
    (apply str (interpose
                 " "
                 (remove #(#{"the" "a" "an"} (s/lower-case %)) 
                         (s/split string #"\ "))))))
(defn- assoc-sort
  [song]
  (assoc song 
         :artist-sort (remove-articles (:artist song))
         :album-sort (remove-articles (:album song))))
(defn- safe-insert
  [db tbl-key]
  (fn
    [m]
    (try
      (apply vals (j/insert! db tbl-key m))
      (catch Exception e
        (log/error (str "failed to insert " m " into songs") e)
        (str e)))))

(defn add-songs!
  [db songs]
  (as->
    (map (comp assoc-sort assoc-file-folder) songs) $
    (map keys->columns $)
    (map (safe-insert db :SONGS) $)
    (reduce #(conj %1 (if (string? %2) %2 (first %2))) [] $)))

(defn add-folders!
  [db folders]
  nil)
;#_(def db
;  {:classname "org.h2.Driver"
;   :subprotocol "h2"
;   :subname (.getAbsolutePath (as-file "test/resources/scratchdb"))})
;#_(j/query db (q/get-all-songs))
