(ns wordhunt.views
  (:require ["@material-ui/core"
             :refer
             [Container
              FormControl
              InputLabel
              MenuItem
              Select
              TextField
              ThemeProvider]]
            ["@material-ui/core/styles" :refer [createTheme]]
            ["@material-ui/core/CssBaseline" :default CssBaseline]
            [re-frame.core :as rf]
            [wordhunt.dictionary :as dictionary]
            [wordhunt.events :as events]
            wordhunt.language
            [wordhunt.subs :as subs]))

(defn header []
  (let [language @(rf/subscribe [::subs/language])
        on-change-language #(rf/dispatch [::events/set-language (.. % -target -value)])

        search-term @(rf/subscribe [::subs/word-query])
        on-change-search-term #(rf/dispatch [::events/word-query-change (.. % -target -value)])]
    [:div.header
     [:span.title (if (< 0 (count search-term))
                    search-term "Word Hunt")]
     [:div.inputs
      [:> TextField {:class "search"
                     :label "Search a Word"
                     :value search-term
                     :on-blur #(rf/dispatch [::events/get-word-info])
                     :on-change on-change-search-term}]
      [:> FormControl {:class "select"}
       [:> InputLabel  "Language"]
       [:> Select
        {:value language
         :on-change on-change-language}
        (map
         (fn [{:keys [value label]}]
           ^{:key value} [:> MenuItem {:value label} value])
         dictionary/languages)
        [:> MenuItem {:value 10} "English"]]]]]))



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
