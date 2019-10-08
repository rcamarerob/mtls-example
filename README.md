Añadir nuevos campos en openssl.cnf de  /etc/ssl :

/etc/ssl/openssl.cnf

```code
oid_section		= new_oids

[ new_oids]
clientAppName=1.2.3.4.5.6

[ req ]
#default_bits		= 2048
#default_md		= sha256
#default_keyfile 	= privkey.pem
distinguished_name	= req_distinguished_name
attributes		= req_attributes

[ req_distinguished_name ]
countryName			= Country Name (2 letter code)
countryName_min			= 2
countryName_max			= 2
stateOrProvinceName		= State or Province Name (full name)
localityName			= Locality Name (eg, city)
0.organizationName		= Organization Name (eg, company)
organizationalUnitName		= Organizational Unit Name (eg, section)
commonName			= Common Name (eg, fully qualified host name)
commonName_max			= 64
emailAddress			= Email Address
emailAddress_max		= 64
clientAppName			= Axa Client Application Name

[ req_attributes ]
challengePassword		= A challenge password
challengePassword_min		= 4
challengePassword_max		= 20
```

—————————————

https://github.com/Hakky54/mutual-tls


Create a keystore for the server,with a public and private key:
```bash
keytool -genkeypair -keyalg RSA -keysize 2048 -alias serverCert -dname "CN=ServerCN,OU=Server,O=Dpto,C=ES" -validity 3650 -keystore serverIdentity.jks -storepass secret -keypass secret -deststoretype pkcs12
```

Client certificate:
```bash
keytool -genkeypair -keyalg RSA -keysize 2048 -alias clientCert -dname "CN=ClientCN,OU=Client,O=RCB,C=ES" -validity 3650 -keystore clientIdentity.jks -storepass secret -keypass secret -deststoretype pkcs12
```


Create trustStore, public key:

* Export client cert

```bash
keytool -exportcert -keystore clientIdentity.jks -storepass secret -alias clientCert -rfc -file client.cer
```

* Export server cert
```bash
keytool -exportcert -keystore serverIdentity.jks -storepass secret -alias serverCert -rfc -file server.cer
```

* Create client truststore with serverCert
```bash
keytool -keystore clientTruststore.jks -importcert -file server.cer -alias serverCert -storepass secret
```

* Create server truststore with clientCert 
```bash
keytool -keystore serverTruststore.jks -importcert -file client.cer -alias clientCert -storepass secret
```


---------------
Testing:

curl -X POST http://localhost:8080/mtls
