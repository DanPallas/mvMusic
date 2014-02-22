(ns mvMusic.library.entities
  (:use [korma db core])
  (:require [mvMusic.library.schema :as sch]
            [clojure.string :as st]))

(defdb db sch/db)

(declare songs albums downloads folders)

(defentity songs
  (table :songs)
  (entity-fields :track :disc :title :artist :length :year :extension :genre
                 :folder :filename :path)
  #_(prepare (fn [{artist :artist :as m}]
             (cond 
               (= (st/lower-case (subs artist 0 4)) "the ")
                  (assoc m :artist (str (subs artist 4) ", The"))
               (= (st/lower-case (subs artist 0 2)) "a ")
                  (assoc m :artist (str (subs artist 2) ", A"))
               :else m)))
  (belongs-to albums)
  (belongs-to folders {:fk :folder})
  (many-to-many downloads :songs-downloads))

(defentity albums
  (table :albums)
  (entity-fields :title :artist :image :sort)
  (has-many songs))

(defentity downloads
  (table :downloads)
  (entity-fields :path :file)
  (many-to-many songs :songs-downloads))

(defentity folders
  (table :folders)
  (entity-fields :path :mod-date))
