(ns wordhunt.views
  (:require ["@material-ui/core" :as comp]
            ["@material-ui/core/styles" :refer [createTheme withStyles]]
            ["@material-ui/core/colors" :refer [grey]]
            ["@material-ui/core/CssBaseline" :default CssBaseline]
            [re-frame.core :as rf]
            [wordhunt.dictionary :as dictionary]
            [wordhunt.events :as events]
            wordhunt.language
            [wordhunt.subs :as subs]
            [clojure.string :as str]))

(def DarkMode ((withStyles (clj->js {"switchBase" {"color"              (aget grey 300)
                                                   "&$checked"          {"color" (aget grey 500)}
                                                   "&$checked + $track" {"backgroundColor" (aget grey 500)}
                                                   "checked"            {}
                                                   "track"              {}}}))
                   comp/Switch))

(defn definitions []
  (let [query    @(rf/subscribe [::subs/word-query])
        meanings @(rf/subscribe [::subs/meanings])
        language @(rf/subscribe [::subs/language])]
    [:div.meanings
     (when (and (first meanings) query (= "en" language))
       [:audio {:style    {:background-color "#fff"
                           :border-radius    "10px"}
                :src      (get-in meanings [0 :phonetics 0 :audio])
                :controls true}
        "Your browser doesn't support autio elements."])
     (if (empty? query)
       [:span.sub-title "Start by typing a word in search"]
       (map
        (fn [mean]
          [:div
           (map
            (fn [item]
              (map
               (fn [{:keys [definition example synonyms]}]
                 [:div.single-mean {:style {:background-color :white
                                            :color            :black}}
                  [:b definition]
                  [:hr {:style {:background-color :black
                                :width            "100%"}}]
                  (when example
                    [:span [:b "Example: "]
                     example])

                  (when-not (empty? synonyms)
                    [:span [:b "Synonyms: "]
                     (str/join ", " synonyms)])
                  ])
               (:definitions item)))
            (:meanings mean))]
          )
        meanings))]))

(defn header []
  (let [language           @(rf/subscribe [::subs/language])
        on-change-language #(rf/dispatch [::events/set-language (.. % -target -value)])

        search-term           @(rf/subscribe [::subs/word-query])
        on-change-search-term #(rf/dispatch [::events/word-query-change (.. % -target -value)])]
    [:div.header
     [:span.title (if (< 0 (count search-term))
                    search-term "Word Hunt")]
     [:div.inputs
      [:> comp/TextField {:class     "search"
                          :label     "Search a Word"
                          :value     search-term
                          :on-change on-change-search-term}]
      [:> comp/FormControl {:class "select"}
       [:> comp/InputLabel  "Language"]
       [:> comp/Select
        {:value     language
         :on-change on-change-language}
        (map
         (fn [{:keys [value label]}]
           ^{:key value} [:> comp/MenuItem {:value label} value])
         dictionary/languages)
        [:> comp/MenuItem {:value 10} "English"]]]]]))



(defn main-panel []
  (let [theme-type (rf/subscribe [::subs/theme])
        theme      (createTheme (clj->js {:palette {:type    @theme-type
                                                    :primary {:main "#fff"}}}))
        dark? (= @theme-type "dark")]
    [:> comp/ThemeProvider {:theme theme}
     [:> CssBaseline]
     [:div.app
      {:style {:height "100vh"
               :background-color (if dark? "#282c34" "#fff")
               :color (if dark? "white" "black")
               :transition "all 0.5s linear"}}

      [:> comp/Container
       {:maxWidth :md
        :style    {:display        :flex
                   :flex-direction :column
                   :height         "100vh"
                   :justifyContent :space-evenly}}
       [:div {:style {:position   :absolute
                      :top        0
                      :right      15
                      :paddingTop 10}}
        [:span (str (str/capitalize @theme-type) " Mode")]
        [:> DarkMode
         {:checked dark?
          :name "checkmark"
          :on-change #(rf/dispatch [::events/toggle-theme])} ]]
       [header]
       [definitions]]]]))
