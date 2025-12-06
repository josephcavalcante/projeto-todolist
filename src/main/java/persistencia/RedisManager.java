package persistencia;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisManager {
    private static RedisManager instance;
    private JedisPool pool;

    private RedisManager() {
        try {
            // Conecta no Redis do Docker na porta padrão 6379
            // Se der erro aqui, verifique se o container está rodando (docker ps)
            this.pool = new JedisPool("localhost", 6379);
            System.out.println("[REDIS] Conexão iniciada com sucesso!");
        } catch (Exception e) {
            System.err.println("[REDIS] ERRO CRÍTICO: Não foi possível conectar ao Redis.");
            System.err.println("[REDIS] O sistema continuará usando apenas o banco SQL.");
        }
    }

    public static synchronized RedisManager getInstance() {
        if (instance == null) {
            instance = new RedisManager();
        }
        return instance;
    }

    public Jedis getJedis() {
        try {
            return pool.getResource();
        } catch (Exception e) {
            System.out.println("[REDIS] Falha ao obter conexão do pool. Verifique o Docker.");
            return null;
        }
    }
}