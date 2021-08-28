(ns wordhunt.views
  (:require
   [re-frame.core :as rf]
   [wordhunt.subs :as subs]
   [wordhunt.events :as events]
   ["@material-ui/core" :refer [Container]]))

(defn header []
  [:div.header
   [:span.title
    "Word Hunt"]])

(defn main-panel []
  (rf/dispatch [::events/get-word-info "plane"])
  (fn []
    (let [name (rf/subscribe [::subs/word])]

      [:div.app
       [header]
       [:> Container
        {:maxWidth :md
         :style {:display :flex
                 :flex-direction :column
                 :height "100vh"}}
        (str @name)]])))
