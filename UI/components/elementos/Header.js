import React from 'react';
import { View, StyleSheet, Text } from 'react-native';
import { Icon } from 'react-native-elements';
import { LinearGradient } from 'expo'

const styles = StyleSheet.create({
  container: {
    padding: 12,
    paddingTop: 28,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    borderBottomColor: 'rgba(255, 255, 255, 0.2)',
    borderBottomWidth: 2
  },
  title: {
    fontSize: 24,
    color: "#FFFFFF"
  }
});

const PlaceholderIcon = () => <View style={{ height: 48, width: 48 }} />;

const Header = ({ leftIcon, leftIconAction, title, rightIcon, rightIconAction }) => (
  <LinearGradient 
    style={styles.container}
    colors={['#312A6C', '#852D91']}
  >
    {leftIcon ?
      <Icon
        name={leftIcon}
        onPress={leftIconAction}
        color="#FFF"
        size={24}
        iconStyle={{ padding: 12 }}
        containerStyle={{ borderRadius: 8 }}
        underlayColor="#0001"
      />
      :
      <PlaceholderIcon />
    }
    <Text bold style={styles.title}>{title}</Text>
    {rightIcon ?
      <Icon
        name={rightIcon}
        onPress={rightIconAction}
        color="#FFF"
        size={24}
        iconStyle={{ padding: 12 }}
        containerStyle={{ borderRadius: 8 }}
        underlayColor="#0001"
      />
      :
      <PlaceholderIcon />
    }
  </LinearGradient>
);

export default Header;
