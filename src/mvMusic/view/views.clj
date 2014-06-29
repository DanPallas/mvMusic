(ns mvMusic.view.views
  (:use [compojure.core]
        [hiccup core page element]))

(defn- wrap-tag
  "Returns a hiccup vector with the given content tag and optional attribute
  map"
  [content tag & [attribute-map]]
  (vec (filter #(not (nil? %1)) (list tag attribute-map content))))

(defn- head
  "Generate hiccup vector for header"
  []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
   (include-css "/foundation/css/normalize.css"  
                "/foundation/css/foundation.css")
   (include-js "/foundation/js/vendor/modernizr.js"
               "http://fb.me/react-0.10.0.js"
               "/foundation/js/vendor/jquery.js"
               "/foundation/js/foundation.min.js")
   [:title "mvMusic" ]])

(defn- nav-bar
  "navigation bar hiccup vector"
  []
  [:nav.top-bar {:data-topbar true}
   [:ul.title-area 
    [:li.name ""]
    [:li.toggle-topbar.menu-icon (link-to "#" [:span "Menu"])]]
   [:section.top-bar-section
    [:ul.right
     [:li.active (link-to "#" "Artists")]
     [:li (link-to "#" "Albums")]
     [:li (link-to "#" "Downloads")]
     [:li (link-to "#" "Settings")]] 
    ]])

(defn- body 
  "Generate vector for body of main page"
  []
  [:body
   [:div#content (include-js "/main.js")]
   [:script "$(document).foundation();"]])

(defn main-page 
  []
  (html5
   (head)
   (body)))

