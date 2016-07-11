# s3-debug

Example for reproducing S3 meta data insertion issue.

## Step 1

Create bucket and setup correct access rights etc. Code will create test.txt file in to root of the bucket.

## Step 2

```bash
# Start repl
$ lein repl
```

## Step 3

```clojure
(require 's3-debug.core)

(s3-debug.core/failing "change-here-bucket-name")
=> com.amazonaws.services.s3.model.AmazonS3Exception: The request signature we calculated does not match the signature you provided. Check your key and signing method. (Service: Amazon S3; Status Code: 403; Error Code: SignatureDoesNotMatch; Request ID: XXXXXXXXXXXXX)

(s3-debug.core/success "change-here-bucket-name") => #<com.amazonaws.services.s3.model.PutObjectResult@xxxxxxx>

```
