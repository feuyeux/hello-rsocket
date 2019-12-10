package common

import (
	"encoding/json"
)

type HelloRequest struct {
	Id string `json:"id"`
}

type HelloRequests struct {
	Ids []string `json:"ids"`
}

type HelloResponse struct {
	Id   string `json:"id"`
	Name string `json:"name"`
}

func (hello *HelloRequest) ToJson() ([]byte, error) {
	var jsonData []byte
	jsonData, err := json.Marshal(hello)
	return jsonData, err
}

func JsonToHelloRequest(jsonData []byte) HelloRequest {
	var hello HelloRequest
	json.Unmarshal(jsonData, &hello)
	return hello
}

func (hello *HelloRequests) ToJson() ([]byte, error) {
	var jsonData []byte
	jsonData, err := json.Marshal(hello)
	return jsonData, err
}

func JsonToHelloRequests(jsonData []byte) HelloRequests {
	var hello HelloRequests
	json.Unmarshal(jsonData, &hello)
	return hello
}
func (hello HelloResponse) ToJson() ([]byte, error) {
	var jsonData []byte
	jsonData, err := json.Marshal(hello)
	return jsonData, err
}

func JsonToHelloResponse(jsonData []byte) HelloResponse {
	var hello HelloResponse
	json.Unmarshal(jsonData, &hello)
	return hello
}
