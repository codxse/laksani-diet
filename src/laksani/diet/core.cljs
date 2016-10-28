(ns laksani.diet.core
  (:require [reagent.core :as r]))

(enable-console-print!)

(def bmi-data (r/atom {:height 180 :weight 80}))

(defn calc-bmi []
  (let [{:keys [height weight bmi] :as data} @bmi-data
        h (/ height 100)]
    (if (nil? bmi)
      (assoc data :bmi (/ weight (* h h)))
      (assoc data :weight (* bmi h h)))))

(defn slider [param value min max]
  [:input {:type "range" :value value :min min :max max
           :style {:width "100%"}
           :on-change (fn [e]
                        (swap! bmi-data assoc param (.-target.value e))
                        (when (not= param :bmi)
                          (swap! bmi-data assoc :bmi nil)))}])

(defn bmi-component []
  (let [{:keys [weight height bmi]} (calc-bmi)
        [color diagnose] (cond
                           (< bmi 18.5) ["orange" "kurus"]
                           (< bmi 25) ["inherit" "ideal"]
                           (< bmi 30) ["orange" "gemuk"]
                           :else ["red" "obesitas"])]
    [:div
     [:h3 "Kalkulator Berat Badan Ideal"]
     [:div
      "Tinggi: " (int height) "cm"
      [slider :height height 100 220]]
     [:div
      "Berat Badans: " (int weight) "kg"
      [slider :weight weight 30 150]]
     [:div
      "BMI: " (int bmi) " "
      [:span {:style {:color color}} diagnose]
      [slider :bmi bmi 10 50]]]))

(r/render-component
  [bmi-component]
  (. js/document (getElementById "app")))

