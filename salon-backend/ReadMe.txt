Local storage (dev)
Physical path: C:\Ravikiran\Practice_Projects\Website\salon-backend\uploads (created automatically, relative to wherever mvnw spring-boot:run is launched from)
Served at: http://localhost:9209/uploads/<filename>
Controlled by app.storage.type=local + app.upload.dir=uploads in application.properties
Production (S3)
I refactored file storage behind a FileStorageService interface so switching is just config, not code:

LocalFileStorageService — active when app.storage.type=local (default)
S3FileStorageService — active when app.storage.type=s3, added to application-prod.properties
To deploy to prod: run with SPRING_PROFILES_ACTIVE=prod and set these env vars:

Env var	Purpose
AWS_S3_BUCKET	target bucket name
AWS_REGION	e.g. us-east-1
AWS_S3_PUBLIC_BASE_URL	optional — set if serving via CloudFront instead of raw S3 URLs
SPRING_DATASOURCE_URL/USERNAME/PASSWORD, JWT_SECRET, CORS_ALLOWED_ORIGINS, ADMIN_DEFAULT_USERNAME/PASSWORD	so prod secrets aren't hardcoded
AWS credentials are never hardcoded — S3Config uses the AWS SDK's default credential chain, so it picks up env vars, an EC2/ECS/EKS IAM role, or ~/.aws/credentials automatically, whichever applies where it's deployed.

Uploaded file URLs: local dev stores imageUrl as /uploads/xyz.jpg; prod stores it as the full https://<bucket>.s3.<region>.amazonaws.com/uploads/xyz.jpg (or your CloudFront domain). No Angular changes were needed — UploadService.resolveUrl() already passes through any URL starting with http as-is.