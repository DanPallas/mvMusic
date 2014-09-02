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
  (s/replace (s/upper-case (name k)) #"-" "_"))

(defn- keys->columns
  [m]
  (reduce 
    (fn [m [k v]] (assoc m (key->column k) v))
    {}
    (seq m)))

(defn- column->key
  [k]
  (keyword (s/replace (s/lower-case (name k)) #"_" "-")))

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

(defn- safe-insert!
  [db tbl-key]
  (fn
    [m]
    (try
      (apply vals (j/insert! db tbl-key m :entities key->column))
      (catch Exception e
        (log/error (str "failed to insert " m " into " (name tbl-key)) e)
        (str e)))))

(defn- update-folder!
  "update folder (map) in db"
  [db folder]
  (j/update! db 
             :FOLDERS 
             folder 
             ["FOLDER = ?" (:folder folder)] 
             :entities key->column))
(defn- update-song!
  "update song in db"
  [db song]
  (j/update! db 
            :SONGS 
            song 
            ["FILE = ?" (:file song)]
            :entities key->column))

(defn- insert-or-update-song!
  [db song]
  (if (empty? (get-songs db :file (:file song)))
      ((safe-insert! db :SONGS) song)
      (update-song! db song)))

(defn- format-results
  "format successful responses to true, unsuccessful responses to error strings"
  [results]
  (reduce 
      (fn [col entry] 
        (conj col (if (string? entry) entry true))) 
      []
      results))

(defn add-songs!
  "adds a song into the library or updates it if it already exists"
  [db songs]
  (->>
    (map (comp assoc-sort assoc-file-folder) songs)
    (map (partial insert-or-update-song! db))
    (format-results)))

(defn get-folder
  [db path]
  (first 
    (j/query db ["SELECT * FROM FOLDERS WHERE FOLDER = ?" path] 
         :identifiers column->key)))

(defn- insert-or-update-folder!
  "update folder if exists or insert"
  [db folder]
  (if (empty? (get-folder db (:folder folder)))
    ((safe-insert! db :FOLDERS) folder)
    (update-folder! db folder)))

(defn add-folders!
  "adds a seq of folders into the library or update mod-dates on existing 
  entries"
  [db folders]
  (->>
    (map (partial insert-or-update-folder! db) folders)
    (format-results)))

(defn add-folder!
  "adds or updates a single folder and returns true, or an error string"
  [db folder]
  (first (add-folders! db [folder])))

(defn- build-where-clause
  [where-cols]
  (str
    " WHERE " 
    (apply str 
           (interpose " AND "
                      (reduce
                        (fn [x k] (conj x (str (key->column k) "=?")))
                        []
                        where-cols)))))
(build-where-clause [:file :title :name])
(defn get-songs
  "get songs with where.
    ex. 'get-songs db :file 'path/file' returns the file where 
        :file = 'path/file'"
  [db & where]
  (let [where (apply hash-map where)
        where-clause (if (empty? where) "" (build-where-clause (keys where)))]
   (j/query db 
            (concat [(str "SELECT * FROM SONGS" where-clause)] (vals where)) 
            :identifiers column->key)))
