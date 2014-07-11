(ns mvMusic.handler
  (:use 
        [mvMusic.global])
  (:require [compojure.core :refer [GET defroutes]] 
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.json :as json]))

(defn artists-dispach
  [artist]
  (if (= artist "all")
    [{:artist "The Beetles" :sort "Beetles" 
      :albums 11 :songs 10 :time 3500},
     {:artist "The Clash" :sort "Clash" 
      :albums 2 :songs 10 :time 1800},
     {:artist "GreenDay" :sort "GreenDay" 
      :albums 5 :songs 11 :time 1000}]
    [{:title (str artist "'s self-titled") :sort artist :length 900 :songs 
      [{:title "song 1" :artist artist :length 80 :codec "flac" :bitrate "12000" 
       :filesize 23000000},
      {:title "song 2" :artist artist :length 120 :codec "mp3" :bitrate "128vbr"
       :filesize 3000000},
      {:title "song 3" :artist artist :length 90 :codec "aac" :bitrate "256"
       :filesize 3500000}]}
     {:title (str artist "purple album") :sort artist :length 1400 :songs 
      [{:title "song 1" :artist artist :length 80 :codec "flac" :bitrate "12000" 
       :filesize 23000000},
      {:title "song 2" :artist artist :length 120 :codec "mp3" :bitrate "128vbr"
       :filesize 3000000},
      {:title "song 3" :artist artist :length 90 :codec "aac" :bitrate "256"
       :filesize 3500000}]
      }]))

(defroutes app-routes
  (GET "/widgets" [] {:body [{:name "Widget 1"} {:name "Widget 2"}]})
  #_(GET [(str "/download/:user-path") :user-path #".*"] 
       [user-path] (dl-file user-path))
  (GET "/artists/:artist" [artist] {:body (artists-dispach artist)})
  (route/resources "/")
  (route/not-found "Not Found"))

(defn wrap-index 
  "converts requests for / to requrests for /index.html"
  [handler]
  (fn [req]
    (handler
      (update-in req [:uri]
                 #(if (= "/" %) "/index.html" %)))))

(def app
  (-> (handler/site app-routes)
      (wrap-index)
      (json/wrap-json-body)
      (json/wrap-json-response)))
