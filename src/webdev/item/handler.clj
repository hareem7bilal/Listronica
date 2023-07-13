(ns webdev.item.handler
  (:require [webdev.item.model :refer [create-item delete-item read-items update-item]]
            [webdev.item.view :refer [items-page]]))

(defn handle-index-items[req]
  (let [db (:webdev/db req) items (read-items db)]
   {:status 200
    :body (items-page items)
    :headers {}}
    ))

(defn handle-create-item [req]
  (let [name (get-in req [:params "name"]) desc (get-in req [:params "description"]) db (:webdev/db req)
        item_id (create-item db name desc)]
    {:status 302
     :body ""
     :headers {"Location" "/items"}}))

(defn handle-delete-item [req]
  (let [db (:webdev/db req) item-id (java.util.UUID/fromString (:item-id (:route-params req)))
        exists? (delete-item db item-id)]
    (if exists? 
      {:status 302
       :body ""
       :headers {"Location" "/items"}}
      
       {:status 404
       :body "Item not found."
       :headers {}}
      )
    ))

(defn handle-update-item [req]
  (let [db (:webdev/db req) item-id (java.util.UUID/fromString (:item-id (:route-params req)))
        checked (get-in req [:params "checked"])
        exists? (update-item db item-id (= "true" checked))]
    (if exists?
      {:status 302
       :body ""
       :headers {"Location" "/items"}}

      {:status 404
       :body "Item not found."
       :headers {}})))