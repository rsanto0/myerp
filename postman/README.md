# 📡 Postman Collections - MyERP

## 🚀 **Integração GitHub + Postman**

### 📋 **Collections Disponíveis**
- **MyERP-System.postman_collection.json** - APIs completas
- **MyERP-Tests.postman_collection.json** - Testes automatizados  
- **MyERP-Environment.postman_environment.json** - Variáveis

### 🔄 **Sincronização Automática**

**GitHub Actions** sincroniza automaticamente:
- ✅ Validação com Newman
- ✅ Upload para Postman Cloud
- ✅ Comentários em PRs

### 🔧 **Configuração**

**1. Secrets do GitHub:**
```
POSTMAN_API_KEY=your-postman-api-key
COLLECTION_UID=your-collection-uid
```

**2. Obter API Key:**
- Postman → Settings → API Keys → Generate

**3. Obter Collection UID:**
- Postman → Collection → Share → Get Link → Extrair UID

### 📤 **Sincronização Manual**
```bash
# Script Windows
scripts\sync-postman.bat

# Ou manual
git add postman/
git commit -m "chore: Atualizar collections"
git push
```

### 🧪 **Testes Locais**
```bash
# Instalar Newman
npm install -g newman

# Testar collection
newman run MyERP-System.postman_collection.json \
  -e MyERP-Environment.postman_environment.json
```

### 🎯 **Fluxo de Trabalho**

1. **Editar** collections no Postman
2. **Exportar** para pasta `postman/`
3. **Commit** no Git
4. **GitHub Actions** sincroniza automaticamente
5. **Postman Cloud** atualizado

### 🔗 **Links Úteis**
- [Postman API Docs](https://documenter.getpostman.com/view/631643/JsLs/)
- [Newman CLI](https://github.com/postmanlabs/newman)
- [GitHub Actions](https://docs.github.com/en/actions)

**Sincronização automática entre Postman e GitHub! 🚀📡**