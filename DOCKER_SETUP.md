# Docker镜像加速器配置指南

## 问题说明
服务器在执行Docker构建时遇到网络连接问题，无法从Docker Hub拉取镜像，显示超时错误。

## 解决方案

### 1. 配置Docker镜像加速器

在服务器上执行以下命令：

```bash
# 创建Docker配置目录
mkdir -p /etc/docker

# 备份现有配置（如果存在）
cp /etc/docker/daemon.json /etc/docker/daemon.json.bak 2>/dev/null || true

# 将新的镜像加速器配置写入文件
cat > /etc/docker/daemon.json <<EOF
{
  "registry-mirrors": [
    "https://registry.docker-cn.com",
    "https://hub-mirror.c.163.com",
    "https://mirror.baidubce.com",
    "https://registry.cn-hangzhou.aliyuncs.com"
  ],
  "insecure-registries": [],
  "debug": false,
  "experimental": false
}
EOF

# 重新加载配置并重启Docker服务
systemctl daemon-reload
systemctl restart docker

# 验证配置是否生效
docker info | grep -A 10 "Registry Mirrors"
```

### 2. 重新运行更新脚本

配置完成后，重新运行更新脚本：

```bash
cd /opt/projects/DragonVVCR
sh update.sh
```

## 注意事项

1. 如果使用阿里云服务器，建议使用阿里云提供的专属镜像加速器，登录阿里云控制台获取。
2. 修改docker-compose-update.yml文件中的镜像源，使用国内镜像源（已修改）。
3. 如果仍然遇到网络问题，可以考虑使用代理或者VPN。

## 其他建议

1. 可以考虑设置Docker构建时的超时时间，在docker-compose文件中添加：
   ```yaml
   build:
     args:
       - BUILDKIT_INLINE_CACHE=1
   ```

2. 如果问题持续存在，可以考虑在本地构建镜像然后推送到私有仓库，在服务器上从私有仓库拉取。
