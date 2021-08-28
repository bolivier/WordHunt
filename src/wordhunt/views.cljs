(ns wordhunt.views
  (:require
   [re-frame.core :as rf]
   [wordhunt.subs :as subs]
   [wordhunt.events :as events]
   ["@material-ui/core" :refer [Container TextField ThemeProvider]]
   ["@material-ui/core/CssBaseline" :default CssBaseline]
   ["@material-ui/core/styles" :refer [createTheme]]))

(defn header []
  [:div.header
   [:span.title "Word Hunt"]
   [:div.inputs
    [:> TextField {:id "standard-basic"
                   :label "Standard"}]]])



(defn main-panel []
  (rf/dispatch [::events/get-word-info "plane"])

  (fn []
    (let [name (rf/subscribe [::subs/word])
          theme-type (rf/subscribe [::subs/theme])
          theme (createTheme (clj->js {:palette {:type @theme-type
                                                 :primary {:main "#fff"}}}))]
      [:> ThemeProvider {:theme theme}
       [:> CssBaseline]
       [:div.app
        [header]
        [:> Container
         {:maxWidth :md
          :style {:display :flex
                  :flex-direction :column
                  :height "100vh"}}
         (str @name)]]])))
