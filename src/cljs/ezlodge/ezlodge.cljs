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

(defn add-peek [id]
  "Appends additional info about hooverd employee to the dom.
  Function is used at the end of display-employee"
  (dom/log (str id))
  (dom/append! (dom/by-id (str "#" id))
  (h/html [:div {:class (str "peek")}
                [:p {:class (str "small")} (str id " is the display id for this emp-peon")]])))


;; Output of getemps
(defn display-employee [json]
  (let [{:keys [display_id first_name last_name logo]} (js->clj json :keywordize-keys true)]
         (dom/log (str display_id))
         (dom/append! (dom/by-id "employees")
         (h/html [:div {:id (str display_id) :class "employee"}
                   [:img {:src (str "../../../media/" logo)}]
                   [:p (str first_name " " last_name)]]))
         ;;(set! (dom/by-id (str display_id)) (setup-employee display_id)))))
         (fn [display_id]
           (em/at js/document
             [(str "#" display_id)] 
               (em/listen :mouseenter 
                 #(em/at (.-currentTarget %) (em/do-> (add-peek display_id))))))))

;; BROKEN
(defn remove-peek [evt]
  "Removes the peek info when the employee is no
  longer hooverd"
  (let [target (ev/target evt)]
    (dom/destroy! (target))
    (dom/destroy! (dom/by-class "peek"))))

(defn reciever [event]
  "xhrio reciever function"
  (let [response (.-target event)]
    (let [employees (.getResponseJson response)]
      (dom/log (str employees))
      (dorun (map display-employee employees)))))

(defn postiever [event]
  "xhrio (post)eiver function"
  (let [response (.-target event)]
    (.write js/document (.getResponseText response))))

(defn getemps [uri reciever]
  "Function to GET employees via xhrio reciever function"
  (xhrio/send uri reciever "GET"))

(defn postemp [uri content]
  "Function to POST employee via xhrio posteiver function"
  (xhrio/send uri postiever "POST" content))

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
    (ev/listen! (dom/by-id "get") :click (fn [evt] (getemps uri reciever)))))
