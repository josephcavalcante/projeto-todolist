package repositorios;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import modelo.Tarefa;
import persistencia.RedisManager;
import redis.clients.jedis.Jedis;
import java.util.List;

public class TarefaCacheRepository {

    private XStream xstream;
    private static final int TTL_SECONDS = 3600; // Cache dura 1 hora

    public TarefaCacheRepository() {
        this.xstream = new XStream();
        // Permissões de segurança para serialização
        this.xstream.addPermission(AnyTypePermission.ANY);
        this.xstream.allowTypesByWildcard(new String[] { "modelo.**", "java.util.**" });
    }

    public void salvarCache(Long idUsuario, List<Tarefa> tarefas) {
        try (Jedis jedis = RedisManager.getInstance().getJedis()) {
            if (jedis == null)
                return;

            String chave = "tarefas:uid:" + idUsuario;
            String xml = xstream.toXML(tarefas);

            jedis.setex(chave, TTL_SECONDS, xml);
            System.out.println("[REDIS] CACHE SAVE -> Dados salvos para UID: " + idUsuario);
        } catch (Exception e) {
            System.out.println("[REDIS] Erro ao salvar: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public List<Tarefa> buscarCache(Long idUsuario) {
        try (Jedis jedis = RedisManager.getInstance().getJedis()) {
            if (jedis == null)
                return null;

            String chave = "tarefas:uid:" + idUsuario;
            System.out.println("[REDIS] Buscando chave: " + chave);

            String xml = jedis.get(chave);

            if (xml != null && !xml.isEmpty()) {
                List<Tarefa> tarefas = (List<Tarefa>) xstream.fromXML(xml);
                System.out.println("[REDIS] HIT! " + tarefas.size() + " tarefas recuperadas da memória rápida.");
                return tarefas;
            }
        } catch (Exception e) {
            System.out.println("[REDIS] Erro ao ler: " + e.getMessage());
        }
        System.out.println("[REDIS] MISS! Chave não encontrada. Será buscado no SQL.");
        return null;
    }

    public void invalidarCache(Long idUsuario) {
        try (Jedis jedis = RedisManager.getInstance().getJedis()) {
            if (jedis == null)
                return;

            jedis.del("tarefas:uid:" + idUsuario);
            System.out.println("[REDIS] INVALIDATE -> Cache limpo para atualização.");
        }
    }
}