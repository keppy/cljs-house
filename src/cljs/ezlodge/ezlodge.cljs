(ns ezlodge.ezlodge
  (:require-macros [hiccups.core :as h]
                   [enfocus.macros :as em])
  (:require [goog.net.XhrIo :as xhrio]
            [goog.json :as json]
            [domina :as dom]
            [domina.css :as css]
            [domina.events :as ev]
            [shoreleave.remotes.request :as request]
            [hiccups.runtime :as hiccupsrt]
            [cljs.reader :refer [read-string]]
            [enfocus.core :as ef]))

(def uri "../../../sbemployee_ember/")

;; Output of getemps
(defn display-employee [json]
  (let [{:keys [display_id first_name last_name logo]} (js->clj json :keywordize-keys true)]
         (dom/log (str display_id))
         (dom/append! (dom/by-id "employees")
         (h/html [:div {:id (str display_id) :class "employee"}
                   [:img {:src (str "../../../media/" logo)}]
                   [:p (str first_name " " last_name)]]))))

;; Shows info on one employee BROKEN
(defn add-peek [evt]
  (let [target (ev/target evt)]
         (dom/log (str target)
         (dom/append! (target)
         (h/html [:div {:class (str "peek")}
                  [:p {:class (str "small")} (str target)]])))))

;; Removes info on one employee BROKEN
(defn remove-peek [evt]
  (let [target (ev/target evt)]
    (dom/destroy! (target))
    (dom/destroy! (dom/by-class "peek"))))

;; Reciever function for xhrio employee load
(defn reciever [event]
  (let [response (.-target event)]
    (let [employees (.getResponseJson response)]
      (dom/log (str employees))
      (dorun (map display-employee employees)))))

(defn posteiver [event]
  (let [response (.-target event)]
    (.write js/document (.getResponseText response))))

;; Callable xhr getter
(defn getemps [uri reciever]
  (xhrio/send uri reciever "GET"))

(defn postemp [uri content]
  (xhrio/send uri posteiver "POST" content))

(em/defaction resize-div [width]
              ["#rz"] (em/chain
                        (em/resize width :curheight 500)
                        (em/resize 5 :curheight 500)))
(em/defaction setup []
              ["#get"] (em/listen :click #(resize-div 200)))
(set! (.-onload js/window) setup)

;; Event handler initialization
(defn ^:export init []
  (when (and js/document
        (aget js/document "getElementById"))
    (ev/listen! (dom/by-id "get") :click (fn [evt] (getemps uri reciever)))
    (ev/listen! (css/sel ".employee") :mouseover (fn [evt] (add-peek evt)))
    (ev/listen! (css/sel ".employee") :mouseout (fn [evt] (remove-peek evt)))))
