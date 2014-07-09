(ns mvMusic.handler
  (:use 
        [mvMusic.global])
  (:require [compojure.core :refer [GET defroutes]] 
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.json :as json]))

(defroutes app-routes
  (GET "/widgets" [] {:body [{:name "Widget 1"} {:name "Widget 2"}]})
  #_(GET [(str "/download/:user-path") :user-path #".*"] 
       [user-path] (dl-file user-path))
  (GET "/artists/:artist" [artist] {:body [{:artist "The Beetles" :sort "Beetles" 
                                     :albums 11 :songs 10 :time "11:55:00"},
                                    {:artist "The Clash" :sort "Clash" 
                                     :albums 2 :songs 10 :time "1:55:00"},
                                    {:artist "GreenDay" :sort "GreenDay" 
                                     :albums 5 :songs 11 :time "9:55:00"}]})
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
