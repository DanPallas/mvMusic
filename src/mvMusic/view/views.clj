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
   ;[:link {:rel "stylesheet" :type "text/css" :href "/main.css"}]
   (include-js "/foundation/js/vendor/modernizr.js")
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
   (include-js "/foundation/js/vendor/jquery.js"
               "/foundation/js/foundation.min.js"
               "http://fb.me/react-0.10.0.js"
               "/js/out/goog/base.js"
               "/js/app.js")
   [:div.row 
    [:div.small-12.medium-4.columns [:h1 "mvMusic"]]
    [:div.small-12.medium-3.columns "search"]]
   (nav-bar)
   [:div#content " "]
   [:script "$(document).foundation();"]
   [:script "goog.require(\"mvMusic.core\");"]
   ])

(defn main-page 
  []
  (html5
   (head)
   (body)))

