import React from 'react';
import { View, Text, StyleSheet, FlatList, TouchableOpacity, Image } from 'react-native';
import { Button, Icon } from 'react-native-elements';

const styles = StyleSheet.create({
  container: {
    marginTop: 8,
    marginBottom: 8,
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center"
  },
  nombre: {
    fontSize: 28,
    textAlign: "left",
    marginLeft: 10
  },
  precio: {
    fontSize: 16,
    textAlign: "right"
  },
  foto: {
    height: 80,
    width: 80
  }
});

class Producto extends React.Component {
  render() {
    const { producto, seleccionar } = this.props;
    return (
      <TouchableOpacity onPress={seleccionar}>
        <View style={styles.container}>
          <View style={{flexDirection: "row", alignItems: "center"}}>
            <Image
              source={producto.foto}
              style={styles.foto}
            />
            <Text style={styles.nombre}>
              {producto.nombre}
            </Text>
          </View>
          <Text style={styles.precio}>
            {producto.precio}
          </Text>
        </View>
      </TouchableOpacity>
    );
  }
}

export default Producto;
