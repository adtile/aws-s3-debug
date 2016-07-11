(ns s3-debug.core
  (:import [com.amazonaws.services.s3 AmazonS3Client]
           [com.amazonaws.services.s3.model CannedAccessControlList
                                            ObjectMetadata
                                            PutObjectRequest]
           [java.io ByteArrayInputStream]))

(defonce client
  (delay
    (AmazonS3Client.)))

(defn store-to-s3 [bucket object-key payload meta-data]
  (println "Storing payload to" bucket "with object-key" object-key "metadata" meta-data)
  (let [key->str (fn [[k v]] [(name k) v])
        bytes (.getBytes payload "UTF-8")
        object-metadata (doto
                          (ObjectMetadata.)
                          (.setCacheControl "cache-control: public, max-age=0, s-maxage=604800")
                          (.setUserMetadata (into {} (map key->str meta-data)))
                          (.setContentType "text/html")
                          (.setContentLength (count bytes)))]
    (.putObject @client (-> (PutObjectRequest. bucket
                                               object-key
                                               (ByteArrayInputStream. bytes)
                                               object-metadata)
                            (.withCannedAcl CannedAccessControlList/PublicRead)))))

(defn failing [bucket]
  (s3-debug.core/store-to-s3 bucket "test.txt" (slurp (clojure.java.io/resource "sample.txt")) {:foobar "360 °  | –"}))

(defn success [bucket]
  (s3-debug.core/store-to-s3 bucket "test.txt" (slurp (clojure.java.io/resource "sample.txt")) {:foobar "360"}))
