FROM golang:1.23-alpine AS builder

WORKDIR /app
COPY go.mod ./
RUN go mod download
COPY . .
RUN go build -o mazerunner cmd/main.go

FROM alpine:latest

WORKDIR /app
COPY --from=builder /app/mazerunner .

ENTRYPOINT ["./mazerunner"]
CMD ["--seed", "42", "--size", "50", "--agents", "all", "--out", "/data"]
