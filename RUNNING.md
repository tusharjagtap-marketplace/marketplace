# Running and Building Guide

This document details the command lines required to clean, compile, run tests, and execute the marketplace microservices architecture locally or in containerized configurations.

---

## 🛠️ Build and Compilation

### 1. Build the Entire Monorepo
To compile all modules and build their respective executable JAR packages:
```bash
# Compile and build package, skipping tests
./mvnw clean package -DskipTests

# Compile and build package, running all unit tests
./mvnw clean package
```

### 2. Build a Specific Microservice Only
To compile and build only a single microservice (which speeds up build times by avoiding compiling unchanged modules):
```bash
# Build Auth Service only
./mvnw clean package -pl auth-service -am -DskipTests

# Build Gateway Service only
./mvnw clean package -pl gateway-service -am -DskipTests

# Build Marketplace Service only
./mvnw clean package -pl marketplace-service -am -DskipTests

# Build Payment Service only
./mvnw clean package -pl payment-service -am -DskipTests
```
*Note: The `-pl` flag specifies the target module name, and the `-am` (also make) flag ensures that any shared dependencies inside the monorepo are compiled first.*

---

## 🧪 Testing

### 1. Run All Tests
```bash
./mvnw clean test
```

### 2. Run Tests for a Specific Service
```bash
# Run Auth Service tests
./mvnw clean test -pl auth-service

# Run Payment Service tests
./mvnw clean test -pl payment-service
```

---

## 🚀 Running the Services

### Option A: Running Locally (Bare Metal)
Start each service in a separate terminal window:

1.  **Auth Service** (Port 9000):
    ```bash
    ./mvnw -pl auth-service spring-boot:run
    ```
2.  **Marketplace Service** (Port 8081):
    ```bash
    ./mvnw -pl marketplace-service spring-boot:run
    ```
3.  **Payment Service** (Port 8082):
    ```bash
    ./mvnw -pl payment-service spring-boot:run
    ```
4.  **Gateway Service** (Port 8080):
    ```bash
    ./mvnw -pl gateway-service spring-boot:run
    ```

---

### Option B: Running Containerized (Docker Compose)
To compile the projects and start all services (along with a local MySQL instance) in containerized mode:

```bash
# Step 1: Package the projects first
./mvnw clean package -DskipTests

# Step 2: Start all containers in the background
docker-compose up --build -d

# Step 3: Check service logs
docker-compose logs -f

# Step 4: Stop all containers
docker-compose down
```

---

### Option C: Running on local Kubernetes (Minikube / Docker Desktop)
Apply the manifests to deploy the entire stack:

```bash
# Deploy all deployments, services, PVCs and configmaps
kubectl apply -f k8s/ --validate=false

# Watch pod status until all pods show "Running"
kubectl get pods -w

# Tear down all resources
kubectl delete -f k8s/
```
