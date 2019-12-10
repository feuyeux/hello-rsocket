package common

import (
	"fmt"

	"github.com/go-redis/redis"
)

type RedisClient struct {
	client *redis.Client
}

func (c *RedisClient) Open(host, port, pw string) {
	c.client = redis.NewClient(&redis.Options{
		Addr:     host + ":" + port,
		Password: pw, // no password set
		DB:       1,  // use default DB
	})
}

//PingPong with redis
func (c *RedisClient) PingPong() {
	pong, _ := c.client.Ping().Result()
	fmt.Println(pong)
}

//Set key,value
func (c *RedisClient) Set(key string, value string) {
	err := c.client.Set(key, value, 0).Err()
	if err != nil {
		panic(err)
	}
}

//Get key
func (c *RedisClient) Get(key string) string {
	val, err := c.client.Get(key).Result()
	if err == redis.Nil {
		fmt.Println(key, " does not exist")
	} else if err != nil {
		panic(err)
	}
	return val
}

//Close redis connection
func (c *RedisClient) Close() {
	c.client.Close()
}
